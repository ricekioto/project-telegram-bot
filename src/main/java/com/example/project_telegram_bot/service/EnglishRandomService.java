package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.entity.SentenceResponse;
import com.example.project_telegram_bot.error.RandomSentenceNotFoundException;
import com.example.project_telegram_bot.error.RandomSentenceServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EnglishRandomService {
    @Value("${url.english.generate-website}")
    private String url;
    private final RequestService requestService;
    private final ParsingService parsingService;
    private final BuildingUrlService buildingUrlService;

    public EnglishRandomService(RequestService requestService, ParsingService parsingService, BuildingUrlService buildingUrlService) {
        this.requestService = requestService;
        this.buildingUrlService = buildingUrlService;
        this.parsingService = parsingService;
    }

    public String getSentence() {
        String url = buildingUrlService.getGeneraterUrl();
        SentenceResponse response = requestService.getSentenceResponse(url);
        if (response == null) {
            throw new RandomSentenceServiceException("Не удалось получить ответ от " + url);
        }
        String sentence = response.getContent();
        if (sentence == null || sentence.trim().isEmpty() || sentence.equals("the element does not exist")) {
            throw new RandomSentenceNotFoundException("Предложение не найдено на странице.");
        }
        return sentence;
    }
}
