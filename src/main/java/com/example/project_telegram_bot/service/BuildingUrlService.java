package com.example.project_telegram_bot.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Service
public class BuildingUrlService {
    @Value("${url.translate-controller}")
    private String translationControllerUrl;
    @Value("${url.random-controller}")
    private String generatorControllerUrl;
    private final String SOURCE_LANG = "en";
    private final String TARGET_LANG = "ru";

    public String getTranslationControllerUrl(String text) {
        String url = UriComponentsBuilder.fromUriString(translationControllerUrl)
                .queryParam("text", text)
                .build()
                .toUriString();
        return url;
    }

}
