package com.example.project_telegram_bot.kafka.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SentenceTranslationRequestDto {
    private UUID requestId;
    private String textToTranslate;
    private String targetLanguage;
}
