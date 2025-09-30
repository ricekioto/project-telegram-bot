package com.example.project_telegram_bot.kafka;

import com.example.project_telegram_bot.kafka.dto.KafkaMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationProducer {

    @Value("${kafka.topic.translation.request}")
    private String translationRequestTopic;

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public void sendTranslationRequest(KafkaMessage message) {
        kafkaTemplate.send(translationRequestTopic, message);
    }

}
