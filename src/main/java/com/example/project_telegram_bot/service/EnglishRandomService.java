package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.error.RandomSentenceNotFoundException;
import com.example.project_telegram_bot.error.RandomSentenceServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EnglishRandomService {
    @Value("${url.english.generate-website}")
    private String url;
    private final RequestService requestService;
    private final ParsingHtmlService parsingHtmlService;
    private final ParsingUrlService parsingUrlService;

    public EnglishRandomService(RequestService requestService, ParsingHtmlService parsingHtmlService, ParsingUrlService parsingUrlService) {
        this.requestService = requestService;
        this.parsingUrlService = parsingUrlService;
        this.parsingHtmlService = parsingHtmlService;
    }

    public String getSentence() {
        String url = parsingUrlService.getGeneraterUrl();
        String html = requestService.getEntity(url);
        if (html == null) {
            throw new RandomSentenceServiceException("Не удалось получить HTML от " + url);
        }
        String sentence = parsingHtmlService.parsingHtmlFromGenerationEnglishSentence(html);
        if (sentence == null || sentence.trim().isEmpty() || sentence.equals("the element does not exist")) {
            throw new RandomSentenceNotFoundException("Предложение не найдено на странице.");
        }
        return sentence;
    }
}
