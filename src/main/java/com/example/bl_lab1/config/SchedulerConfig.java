package com.example.bl_lab1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    //need to define your service class here
    
    private final int delayTime = 30000;
    
    public SchedulerConfig() {
        //this.service = service;
    }
    
    @Scheduled(cron = "0 36 18 * * *", zone = "Europe/Moscow")
    public void updateSectionWithLastVersion() {
        //service.deleteDeclinedVersions();
    }
}
