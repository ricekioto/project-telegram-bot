package com.example.project_telegram_bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class BuildingUrlService {
    @Value("${url.english.translate-website}")
    private String translationUrl;
    @Value("${url.english.generate-website}")
    private String generatorUrl;
    private final String SOURCE_LANG = "en"; // Язык оригинала
    private final String TARGET_LANG = "ru";
    private final String COUNT = "1";
    private final String LANGUAGE = "eng";


    public String getTranslationUrl(String text) {
        String encodedText = UriComponentsBuilder.fromUriString(text).build().encode().toString();

        String url = UriComponentsBuilder.fromUriString(translationUrl)
                .queryParam("source_lang", SOURCE_LANG)
                .queryParam("target_lang", TARGET_LANG)
                .queryParam("text", encodedText)
                .build()
                .toUriString();

        return url;
    }

    public String getGeneraterUrl() {
        String url = UriComponentsBuilder.fromUriString(generatorUrl)
                .queryParam("language", LANGUAGE)
                .queryParam("count", COUNT)
                .build()
                .toUriString();
        return url;
    }
}
