package com.example.project_telegram_bot.service;

import org.springframework.stereotype.Service;

@Service
public class TranslatorService {
    private final ParsingService parsingService;
    private final BuildingUrlService buildingUrlService;
    private final RequestService requestService;

    public TranslatorService(ParsingService parsingService, BuildingUrlService buildingUrlService, RequestService requestService) {
        this.parsingService = parsingService;
        this.buildingUrlService = buildingUrlService;
        this.requestService = requestService;
    }

    public String getTranslatedText(String text) {
        String validateUrl = buildingUrlService.getTranslationUrl(text);
        String html = requestService.getHtml(validateUrl);
        String translatedText = parsingService.parsingHtmlFromTranslator(html);
        return translatedText;
    }
}
