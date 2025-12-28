package com.cae.scheduler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SchedulerEvents {

    public static final SchedulerEvents SINGLETON = new SchedulerEvents();

    private final List<SchedulerEventsSubscriber> subscribers = new ArrayList<>();

    public void emit(SchedulerEvent schedulerEvent){
        this.subscribers.forEach(subscriber -> subscriber.receive(schedulerEvent));
    }

    public SchedulerEvents subscribe(SchedulerEventsSubscriber subscriber){
        this.subscribers.add(subscriber);
        return this;
    }

}
