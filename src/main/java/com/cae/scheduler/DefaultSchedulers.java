package com.cae.scheduler;

import com.cae.mapped_exceptions.specifics.InternalMappedException;

import java.util.Optional;

public class DefaultSchedulers {

    public static void start(DefaultScheduler defaultScheduler){
        var schedulerName = defaultScheduler.getClass().getSimpleName();
        var setup = defaultScheduler.getSetup();
        var scheduler = defaultScheduler.getExecutor();
        var setupTimeUnit = setup.getTimeUnit();
        var initialDelay = Optional.ofNullable(setup.getInitialDelay()).orElseThrow(() -> new InternalMappedException(
                "Couldn't start " + schedulerName,
                "'initialDelay' setup field was null"
        ));
        var interval = Optional.ofNullable(setup.getIntervals()).orElseThrow(() -> new InternalMappedException(
                "Couldn't start " + schedulerName,
                "'intervals' setup field was null"
        ));
        if (setup.getFixedRate())
            scheduler.scheduleAtFixedRate(defaultScheduler::run, initialDelay, interval, setupTimeUnit);
        else
            scheduler.scheduleWithFixedDelay(defaultScheduler::run, initialDelay, interval, setupTimeUnit);
        var runtime = Runtime.getRuntime();
        var shutdownThread = new Thread(defaultScheduler::shutdown, schedulerName + "ShutdownHook");
        runtime.addShutdownHook(shutdownThread);
    }

}
