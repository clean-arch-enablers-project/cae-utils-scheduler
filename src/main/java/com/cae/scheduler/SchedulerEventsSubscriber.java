package com.cae.scheduler;

public interface SchedulerEventsSubscriber {

    void receive(SchedulerEvent schedulerEvent);

}
