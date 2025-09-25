package com.example.project_telegram_bot.kafka; // Предполагаемый пакет

import com.example.project_telegram_bot.kafka.dto.KafkaMessage;
import com.example.project_telegram_bot.service.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationConsumer {

    private final BotService botService;

    @KafkaListener(topics = "${kafka.topic.translation.response}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void listenTranslationResponse(KafkaMessage message) {
        Long chatId = message.getChatId();

        String fullMessage = botService.getFullMessage(
                message.getGeneratedSentence(),
                message.getTranslatedSentence(),
                chatId);
        botService.sendMessage(fullMessage, chatId);
    }
}
