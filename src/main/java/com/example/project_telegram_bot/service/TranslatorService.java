package com.example.project_telegram_bot.service;

import org.springframework.stereotype.Service;

@Service
public class TranslatorService {
    private final ParsingHtmlService parsingHtmlService;
    private final ParsingUrlService parsingUrlService;
    private final RequestService requestService;

    public TranslatorService(ParsingHtmlService parsingHtmlService, ParsingUrlService parsingUrlService, RequestService requestService) {
        this.parsingHtmlService = parsingHtmlService;
        this.parsingUrlService = parsingUrlService;
        this.requestService = requestService;
    }

    public String getTranslatedText(String text) {
        String validateUrl = parsingUrlService.buildTranslationUrl(text);
        String html = requestService.getEntity(validateUrl);
        String translatedText = parsingHtmlService.parsingHtmlFromTranslator(html);
        return translatedText;
    }
}
