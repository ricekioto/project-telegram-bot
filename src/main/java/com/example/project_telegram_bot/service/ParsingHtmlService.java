package com.example.project_telegram_bot.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class ParsingHtmlService {

    private String cssSelectorForGeneration = "fs-40px bold";
    private String cssSelectorForTranslator = "translation-word translation-chunk";

    public String parsingHtmlFromGenerationEnglishSentence(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByClass(cssSelectorForGeneration);
        if (elements.size() == 0)
            return "the element does not exist";
        return elements.getFirst().text();
    }

    public String parsingHtmlFromTranslator(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByClass(cssSelectorForTranslator);
        if (elements.size() == 0)
            return "the element does not exist";
        return elements.getFirst().text();
    }
}
