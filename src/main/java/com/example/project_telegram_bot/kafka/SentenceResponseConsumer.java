package com.example.project_telegram_bot.kafka;

import com.example.project_telegram_bot.kafka.dto.SentenceGenerationResponseDto;
import com.example.project_telegram_bot.service.RequestStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentenceResponseConsumer {

    private static final Logger logger = LoggerFactory.getLogger(SentenceResponseConsumer.class);

    @Value("${kafka.topic.sentence.response}")
    private String sentenceResponseTopic;

    private final TranslationRequestProducer translationRequestProducer;
    private final RequestStateService requestStateService;

    public SentenceResponseConsumer(TranslationRequestProducer translationRequestProducer,
                                    RequestStateService requestStateService) {
        this.translationRequestProducer = translationRequestProducer;
        this.requestStateService = requestStateService;
    }

    @KafkaListener(topics = "${kafka.topic.sentence.response}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void listenSentenceResponse(SentenceGenerationResponseDto message, String key) {
        logger.info("Received message='{}' with key='{}' from topic '{}'",
                message, key, sentenceResponseTopic);

        Long chatId = requestStateService.getChatIdByRequestId(message.getRequestId());

        if (chatId != null) {
            requestStateService.addGeneratedSentence(message.getRequestId(), message.getGeneratedSentence());

            translationRequestProducer.requestTranslation(message.getGeneratedSentence(), "ru", chatId);
        } else {
            logger.warn("Chat ID not found for requestId: {}. Message content: {}", message.getRequestId(), message);
        }
    }
}