package com.cae.scheduler;

import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
@Builder
public class DefaultSchedulerSetup {

    @Builder.Default
    private Boolean fixedRate = false;
    private Integer initialDelay;
    private Integer intervals;
    @Builder.Default
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

}
