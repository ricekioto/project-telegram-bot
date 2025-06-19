package com.example.project_telegram_bot.service;

import org.springframework.stereotype.Service;

@Service
public class TranslatorService {
    private final ParsingHtmlService parsingHtmlService;
    private final BuildingUrlService buildingUrlService;
    private final RequestService requestService;

    public TranslatorService(ParsingHtmlService parsingHtmlService, BuildingUrlService buildingUrlService, RequestService requestService) {
        this.parsingHtmlService = parsingHtmlService;
        this.buildingUrlService = buildingUrlService;
        this.requestService = requestService;
    }

    public String getTranslatedText(String text) {
        String validateUrl = buildingUrlService.getTranslationUrl(text);
        String html = requestService.getHtml(validateUrl);
        String translatedText = parsingHtmlService.parsingHtmlFromTranslator(html);
        return translatedText;
    }
}
