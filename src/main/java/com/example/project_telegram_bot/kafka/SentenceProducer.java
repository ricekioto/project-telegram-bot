package com.example.project_telegram_bot.kafka;

import com.example.project_telegram_bot.kafka.dto.KafkaMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SentenceProducer {

    @Value("${kafka.topic.sentence.request}")
    private String sentenceRequestTopic;

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public void sendSentenceGenerationRequest(KafkaMessage message) {
        kafkaTemplate.send(sentenceRequestTopic, message);
    }

    public void requestNewSentence(Long chatId) {
        KafkaMessage message = KafkaMessage.builder()
                .chatId(chatId)
                .build();
        sendSentenceGenerationRequest(message);
    }

}