package com.hellodk.wb_hotsearch.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

/**
 * @author: hellodk
 * @description Schedule Service
 * @date: 10/19/2021 1:16 PM
 */

@Service
@EnableScheduling
public class ScheduleService {

    @Bean
    public TaskScheduler poolScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("wb");
        scheduler.setPoolSize(10);
        return scheduler;
    }

//    @Async(value = "asyncPoolTaskExecutor")
//    @Scheduled(cron = "*/5 * * * * ?")
//    public void test() {
//        System.out.println("bbb");
//    }

}
