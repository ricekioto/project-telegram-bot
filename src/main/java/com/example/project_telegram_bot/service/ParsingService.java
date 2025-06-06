package com.example.project_telegram_bot.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ParsingService {

    private String cssSelector = "fs-40px bold";

    public String parsingHtml(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByClass(cssSelector);
        return elements.getFirst().text();
    }
}
