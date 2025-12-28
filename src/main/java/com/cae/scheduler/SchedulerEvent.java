package com.cae.scheduler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SchedulerEvent {

    public static SchedulerEvent of(String message){
        return new SchedulerEvent(message);
    }

    private final String message;

}
