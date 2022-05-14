package com.tistory.jaimemin.studyrecruitment.infra.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author jaime
 * @title AsyncConfig
 * @see\n <pre>
 * </pre>
 * @since 2022-05-14
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("processor count {}", processors);

        // CPU
        taskExecutor.setCorePoolSize(processors);
        taskExecutor.setMaxPoolSize(processors * 2);
        // CPU
        // Memory
        taskExecutor.setQueueCapacity(50);
        // Memory
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setThreadNamePrefix("AsyncExecutor-");
        taskExecutor.initialize();

        return taskExecutor;
    }

}
