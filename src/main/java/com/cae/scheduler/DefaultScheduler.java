package com.cae.scheduler;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;

public abstract class DefaultScheduler extends Scheduler<DefaultSchedulerSetup> {

    protected DefaultScheduler(Function<Scheduler<?>, ScheduledExecutorService> provider) {
        super(provider);
        DefaultSchedulers.start(this);
    }

    protected DefaultScheduler() {
        DefaultSchedulers.start(this);
    }

}
