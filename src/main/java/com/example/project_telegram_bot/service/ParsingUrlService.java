package com.example.project_telegram_bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ParsingUrlService {
    @Value("${url.english.translate-website}")
    private String translationUrl;
    private final String SOURCE_LANG = "en"; // Язык оригинала
    private final String TARGET_LANG = "ru"; // Язык перевода

    public String buildTranslationUrl(String text) {
        String encodedText = UriComponentsBuilder.fromUriString(text).build().encode().toString();

        String url = UriComponentsBuilder.fromUriString(translationUrl)
                .queryParam("source_lang", SOURCE_LANG)
                .queryParam("target_lang", TARGET_LANG)
                .queryParam("text", encodedText)
                .build()
                .toUriString();

        return url;
    }
}
