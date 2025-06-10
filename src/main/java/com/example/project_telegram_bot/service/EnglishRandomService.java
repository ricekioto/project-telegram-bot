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

    public EnglishRandomService(RequestService requestService, ParsingHtmlService parsingHtmlService) {
        this.requestService = requestService;
        this.parsingHtmlService = parsingHtmlService;
    }

    public String getSentence() {
        try {
            String html = requestService.getHtml(url);
            if (html == null) {
                throw new RandomSentenceServiceException("Не удалось получить HTML от " + url);
            }
            String sentence = parsingHtmlService.parsingHtmlFromGenerationEnglishSentence(html);
            if (sentence == null || sentence.trim().isEmpty() || sentence.equals("the element does not exist")) {
                throw new RandomSentenceNotFoundException("Предложение не найдено на странице.");
            }
            return sentence;
        } catch (RandomSentenceNotFoundException e) {
            throw e;
        } catch (RandomSentenceServiceException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RandomSentenceServiceException("Неожиданная ошибка при получении предложения: " + e.getMessage(), e);
        }
    }
}
