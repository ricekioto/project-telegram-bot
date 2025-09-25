package com.example.project_telegram_bot.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${kafka.topic.sentence.request}")
    private String sentenceRequestTopic;

    @Value("${kafka.topic.sentence.response}")
    private String sentenceResponseTopic;

    @Value("${kafka.topic.translation.request}")
    private String translationRequestTopic;

    @Value("${kafka.topic.translation.response}")
    private String translationResponseTopic;

    @Bean
    public NewTopic sentenceRequestTopic() {
        return TopicBuilder.name(sentenceRequestTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sentenceResponseTopic() {
        return TopicBuilder.name(sentenceResponseTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic translationRequestTopic() {
        return TopicBuilder.name(translationRequestTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic translationResponseTopic() {
        return TopicBuilder.name(translationResponseTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}