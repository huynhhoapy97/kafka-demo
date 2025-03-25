package com.huynhhoapy97;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumer {
    @KafkaListener(topics = "topic-spring-boot", groupId = "order-group")
    public void listen(String message) {
        System.out.println(message);
    }
}
