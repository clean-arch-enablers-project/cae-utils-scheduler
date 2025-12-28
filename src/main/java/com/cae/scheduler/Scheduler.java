package com.cae.scheduler;

import com.cae.mapped_exceptions.specifics.InternalMappedException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Getter
public abstract class Scheduler<S> {

    protected Scheduler(){
        this.executor = Executors.newSingleThreadScheduledExecutor(new SchedulerThreadFactory(this));
    }

    protected Scheduler(Function<Scheduler<?>, ScheduledExecutorService> provider){
        this.executor = provider.apply(this);
    }

    protected final ScheduledExecutorService executor;
    protected S setup;

    protected abstract S provideSetup();

    public S getSetup(){
        return Optional.ofNullable(this.setup).orElseGet(() -> {
            this.setup = Optional.ofNullable(this.provideSetup())
                .orElseThrow(() -> new InternalMappedException(
                    "Null setup",
                    "You must provide a setup instance at the " + this.getClass().getSimpleName()
                ));
            return this.setup;
        });
    }

    public abstract void run();

    public void shutdown() {
        var message = "Shutting '" + this.getClass().getSimpleName() + "' down";
        SchedulerEvents.SINGLETON.emit(SchedulerEvent.of(message));
        if (this.executor != null) {
            this.executor.shutdown();
            try {
                if (!this.executor.awaitTermination(8, TimeUnit.SECONDS)) {
                    SchedulerEvents.SINGLETON.emit(SchedulerEvent.of(message + ": FORCED"));
                    this.executor.shutdownNow();
                }
                SchedulerEvents.SINGLETON.emit(SchedulerEvent.of(message + ": SUCCESS"));
            } catch (InterruptedException e) {
                this.executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    @RequiredArgsConstructor
    public static class SchedulerThreadFactory implements ThreadFactory {

        private final Scheduler<?> runner;

        @Override
        public Thread newThread(@NonNull Runnable task) {
            var newThread = new Thread(task, this.runner.getClass().getSimpleName());
            newThread.setDaemon(false);
            return newThread;
        }
    }

}
