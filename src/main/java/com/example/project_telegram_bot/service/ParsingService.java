package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.error.ElementNotFoundException;
import com.example.project_telegram_bot.error.ParsingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class ParsingService {

    private String cssSelectorForGeneration = "fs-40px bold";
    private String cssSelectorForTranslator = "translation-word translation-chunk";

    public String parsingHtmlFromGenerationEnglishSentence(String html) throws ParsingException {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByClass(cssSelectorForGeneration);
        if (elements.isEmpty()) {
            throw new ElementNotFoundException("Элемент с классом '" + cssSelectorForGeneration + "' не найден.");
        }
        String text = elements.first().text();
        return text;
    }

    public String parsingHtmlFromTranslator(String html) throws ParsingException {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByClass(cssSelectorForTranslator);
        if (elements.isEmpty()) {
            throw new ElementNotFoundException("Элемент с классом '" + cssSelectorForTranslator + "' не найден.");
        }
        String text = elements.first().text();
        return text;
    }
}
