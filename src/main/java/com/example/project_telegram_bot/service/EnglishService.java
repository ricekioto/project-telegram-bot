package com.example.project_telegram_bot.service;

import org.springframework.stereotype.Service;

@Service
public class EnglishService {
    private RequestService requestService;
    private ParsingService parsingService;

    public EnglishService(RequestService requestService, ParsingService parsingService) {
        this.requestService = requestService;
        this.parsingService = parsingService;
    }

    public String getSentence() {
        String html = requestService.getHtml();
        String sentence = parsingService.parsingHtml(html);
        return sentence;
    }
}
