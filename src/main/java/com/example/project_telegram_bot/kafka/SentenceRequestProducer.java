package com.example.project_telegram_bot.kafka;

import com.example.project_telegram_bot.kafka.dto.SentenceGenerationRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SentenceRequestProducer {

    private static final Logger logger = LoggerFactory.getLogger(SentenceRequestProducer.class);

    @Value("${kafka.topic.sentence.request}")
    private String sentenceRequestTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public SentenceRequestProducer(KafkaTemplate<String, Object> sentenceRequestKafkaTemplate) {
        this.kafkaTemplate = sentenceRequestKafkaTemplate;
    }

    public void sendSentenceGenerationRequest(SentenceGenerationRequestDto message, String key) {
        kafkaTemplate.send(sentenceRequestTopic, key, message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("Sent message='{}' with key='{}' to topic '{}'",
                                message, key, sentenceRequestTopic);
                    } else {
                        logger.error("Unable to send message='{}' with key='{}' to topic '{}', reason: {}",
                                message, key, sentenceRequestTopic, ex.getMessage());
                    }
                });
    }

    public void requestNewSentence(Long chatId) {
        UUID requestId = UUID.randomUUID();
        SentenceGenerationRequestDto requestDto = new SentenceGenerationRequestDto(requestId);
        String messageKey = String.valueOf(chatId);

        sendSentenceGenerationRequest(requestDto, messageKey);
    }
}


