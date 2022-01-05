package com.uxlt.project.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

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
@Slf4j
public class ScheduleTest {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaOutTemplate;

    @Scheduled(cron = "${scheduling.cron.test:-}")
    //@Scheduled(fixedRate=5000)直接指定时间间隔5秒
    private void configureTasks() {
        System.err.println("定时任务: " + LocalDateTime.now());
        send("aaa1", "bbb1", "ccc1");
        sendOut("aaa2", "bbb2", "ccc2");
    }

    public ListenableFuture<SendResult<String, String>> send(String topic, String key, String json) {
        ListenableFuture<SendResult<String, String>> result = kafkaTemplate.send(topic, key, json);
        log.info("inner kafka send #topic=" + topic + "#key=" + key + "#json=" + json + "#推送成功===========");
        return result;
    }

    public ListenableFuture<SendResult<String, String>> sendOut(String topic, String key, String json) {
        ListenableFuture<SendResult<String, String>> result = kafkaOutTemplate.send(topic, key, json);
        log.info("out kafka send #topic=" + topic + "#key=" + key + "#json=" + json + "#推送成功===========");
        return result;
    }
}