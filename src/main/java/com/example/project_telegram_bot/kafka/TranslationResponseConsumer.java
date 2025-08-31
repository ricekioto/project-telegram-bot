package com.example.project_telegram_bot.kafka; // Предполагаемый пакет

import com.example.project_telegram_bot.entity.Constants;
import com.example.project_telegram_bot.kafka.dto.SentenceTranslationResponseDto;
import com.example.project_telegram_bot.service.BotService;
import com.example.project_telegram_bot.service.BuildingUrlService;
import com.example.project_telegram_bot.service.KeyboardFactoryService;
import com.example.project_telegram_bot.service.RequestStateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TranslationResponseConsumer {
    private static final Logger logger = LoggerFactory.getLogger(TranslationResponseConsumer.class);

    @Value("${kafka.topic.translation.response}")
    private String translationResponseTopic;

    private final RequestStateService requestStateService;
    private final BotService botService;

    @KafkaListener(topics = "${kafka.topic.translation.response}",
            groupId = "${kafka.consumer.group-id}")
    public void listenTranslationResponse(SentenceTranslationResponseDto message, String key) {
        logger.info("Received message='{}' with key='{}' from topic '{}'",
                message, key, translationResponseTopic);

        Long chatId = requestStateService.getChatIdByRequestId(message.getRequestId());

        if (chatId != null) {
            String generatedSentence = requestStateService.getGeneratedSentenceByRequestId(message.getRequestId());
            if (generatedSentence != null) {
                String translateText = message.getTranslatedText();
                if (translateText != null) {
                    String fullMessage = botService.getFullMessage(generatedSentence, translateText, chatId);
                    botService.sendMessage(fullMessage, chatId);
                    requestStateService.removeRequestIdMapping(message.getRequestId());
                } else {
                    logger.warn("Translated sentence not found for requestId: {}. Message content: {}", message.getRequestId(), message);
                }
            } else {
                logger.warn("Generated sentence not found for requestId: {}. Message content: {}", message.getRequestId(), message);
            }
        } else {
            logger.warn("Chat ID not found for requestId: {}. Message content: {}", message.getRequestId(), message);
        }
    }
}
