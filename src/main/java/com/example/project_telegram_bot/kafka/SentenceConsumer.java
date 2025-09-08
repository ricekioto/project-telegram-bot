package com.example.project_telegram_bot.kafka;

import com.example.project_telegram_bot.kafka.dto.KafkaMessage;
import com.example.project_telegram_bot.service.RequestStateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentenceConsumer {

    @Value("${kafka.topic.sentence.response}")
    private String sentenceResponseTopic;

    private final TranslationProducer translationProducer;
    private final RequestStateService requestStateService;

    public SentenceConsumer(TranslationProducer translationProducer,
                            RequestStateService requestStateService) {
        this.translationProducer = translationProducer;
        this.requestStateService = requestStateService;
    }

    @KafkaListener(topics = "${kafka.topic.sentence.response}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void listenSentenceResponse(KafkaMessage message) throws KafkaException {
        translationProducer.sendTranslationRequest(message);
    }
}