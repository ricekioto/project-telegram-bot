package com.example.project_telegram_bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EnglishService {
    @Value("${url.english.generate-website}")
    private String url;
    private final RequestService requestService;
    private final ParsingHtmlService parsingHtmlService;

    public EnglishService(RequestService requestService, ParsingHtmlService parsingHtmlService) {
        this.requestService = requestService;
        this.parsingHtmlService = parsingHtmlService;
    }

    public String getSentence() {
        String html = requestService.getHtml(url);
        String sentence = parsingHtmlService.parsingHtmlFromGenerationEnglishSentence(html);
        return sentence;
    }
}
