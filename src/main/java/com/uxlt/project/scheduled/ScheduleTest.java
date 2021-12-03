package com.uxlt.project.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * ScheduleTaskConfig
 * <p>
 * <br/>
 *
 * @author apr
 * @date 2021/12/03 10:11:44
 **/
@Component
public class ScheduleTest {
    @Scheduled(cron = "${scheduling.cron.test:-}")
    //@Scheduled(fixedRate=5000)直接指定时间间隔5秒
    private void configureTasks() {
        System.err.println("定时任务: " + LocalDateTime.now());
    }
}