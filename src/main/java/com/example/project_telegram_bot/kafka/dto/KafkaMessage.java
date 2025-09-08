package com.example.project_telegram_bot.kafka.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessage {
    private Long chatId;
    private String generatedSentence;
    private String translatedSentence;
}