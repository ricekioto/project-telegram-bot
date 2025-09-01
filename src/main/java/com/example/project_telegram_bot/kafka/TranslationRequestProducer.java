package com.example.project_telegram_bot.kafka;

import com.example.project_telegram_bot.kafka.dto.SentenceTranslationRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TranslationRequestProducer {

    private static final Logger logger = LoggerFactory.getLogger(TranslationRequestProducer.class);

    @Value("${kafka.topic.translation.request}")
    private String translationRequestTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public TranslationRequestProducer(KafkaTemplate<String, Object> translationRequestKafkaTemplate) {
        this.kafkaTemplate = translationRequestKafkaTemplate;
    }

    public void sendTranslationRequest(SentenceTranslationRequestDto message, String key) {
        kafkaTemplate.send(translationRequestTopic, key, message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("Sent message='{}' with key='{}' to topic '{}'",
                                message, key, translationRequestTopic);
                    } else {
                        logger.error("Unable to send message='{}' with key='{}' to topic '{}', reason: {}",
                                message, key, translationRequestTopic, ex.getMessage());
                    }
                });
    }

    public void requestTranslation(String textToTranslate, String targetLanguage, Long chatId) {
        UUID requestId = UUID.randomUUID();
        SentenceTranslationRequestDto requestDto = new SentenceTranslationRequestDto(requestId, textToTranslate, targetLanguage);
        String messageKey = String.valueOf(chatId);

        sendTranslationRequest(requestDto, messageKey);
    }
}

