package com.huynhhoapy97.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public NewTopic notification() {
        // (topic name, partition numbers, replication number of each partition in topic)
        return new NewTopic("notification", 3, (short) 3);
    }

    @Bean
    public NewTopic statistic() {
        // (topic name, partition numbers, replication number of each partition in topic)
        return new NewTopic("statistic", 1, (short) 3);
    }
}
