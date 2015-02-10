package de.miq.dirama.server.config;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration for asynchronous tasks.
 * 
 * @author mkuss
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    private static final int QUEUE_CAPACITY = 100;
    private static final int WAIT_FOR_TASK_IF_QUEUE_FULL = 500;

    @Value("${async.poolSize}")
    private int poolSize;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("AsyncExe-");

        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(final Runnable r,
                    final ThreadPoolExecutor executor) {
                while (true) {
                    try {
                        executor.getQueue().put(r);
                        return;
                    } catch (InterruptedException e) {
                        try {
                            Thread.sleep(WAIT_FOR_TASK_IF_QUEUE_FULL);
                        } catch (InterruptedException e1) {
                            // ignore
                        }
                    }
                }
            }
        });
        executor.afterPropertiesSet();

        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
