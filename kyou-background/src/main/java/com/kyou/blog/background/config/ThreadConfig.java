package com.kyou.blog.background.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author CC
 * time 2023-07-28
 * description
 */
@Configuration
@EnableAsync
public class ThreadConfig {

    @Bean
    public Executor executor(){
        ThreadPoolTaskExecutor config = new ThreadPoolTaskExecutor();
        config.setCorePoolSize(5);
        config.setMaxPoolSize(20);
        config.setQueueCapacity(Integer.MAX_VALUE);
        config.setKeepAliveSeconds(60);
        //默认线程名称
        config.setThreadNamePrefix("kyou");
        //等待所有任务结束后再关闭线程池
        config.setWaitForTasksToCompleteOnShutdown(true);
        //执行初始化
        config.initialize();
        return config;

    }




}
