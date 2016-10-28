package com.santex.service.implementation;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

@Service
public class ThreadPoolShutdownService implements ApplicationListener<ContextClosedEvent> {
    private final ThreadPoolTaskExecutor executor;
    private final ThreadPoolTaskScheduler scheduler;

    public ThreadPoolShutdownService(ThreadPoolTaskExecutor executor, ThreadPoolTaskScheduler scheduler) {
        this.executor = executor;
        this.scheduler = scheduler;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        executor.shutdown();
        scheduler.shutdown();
    }
}
