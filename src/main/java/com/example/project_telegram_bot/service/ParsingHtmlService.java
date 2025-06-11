package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.error.ElementNotFoundException;
import com.example.project_telegram_bot.error.ParsingHtmlException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ParsingHtmlService {

    private static final Logger logger = LoggerFactory.getLogger(ParsingHtmlService.class);

    private String cssSelectorForGeneration = "fs-40px bold";
    private String cssSelectorForTranslator = "translation-word translation-chunk";

    public String parsingHtmlFromGenerationEnglishSentence(String html) {
        logger.info("Парсинг HTML для генерации английского предложения.");
        try {
            Document doc = Jsoup.parse(html);
            Elements elements = doc.getElementsByClass(cssSelectorForGeneration);
            if (elements.isEmpty()) {
                logger.warn("Элемент с классом '{}' не найден.", cssSelectorForGeneration);
                throw new ElementNotFoundException("Элемент с классом '" + cssSelectorForGeneration + "' не найден.");
            }
            String text = elements.first().text();
            logger.info("Успешно извлечен текст: {}", text);
            return text;
        } catch (Exception e) {
            logger.error("Ошибка при парсинге HTML для генерации английского предложения: {}", e.getMessage(), e);
            throw new ParsingHtmlException("Ошибка при парсинге HTML для генерации английского предложения: " + e.getMessage(), e);
        }
    }

    public String parsingHtmlFromTranslator(String html) {
        logger.info("Парсинг HTML для перевода.");
        try {
            Document doc = Jsoup.parse(html);
            Elements elements = doc.getElementsByClass(cssSelectorForTranslator);
            if (elements.isEmpty()) {
                logger.warn("Элемент с классом '{}' не найден.", cssSelectorForTranslator);
                throw new ElementNotFoundException("Элемент с классом '" + cssSelectorForTranslator + "' не найден.");
            }
            String text = elements.first().text();
            logger.info("Успешно извлечен текст: {}", text);
            return text;
        } catch (Exception e) {
            logger.error("Ошибка при парсинге HTML для перевода: {}", e.getMessage(), e);
            throw new ParsingHtmlException("Ошибка при парсинге HTML для перевода: " + e.getMessage(), e);
        }
    }
}
