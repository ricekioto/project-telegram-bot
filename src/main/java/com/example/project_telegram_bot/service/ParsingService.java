package com.example.project_telegram_bot.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ParsingService {

    private String cssSelector = "fs-40px bold";

    public String parsingHtml(Path pathFileHtml) throws IOException {
        String html = Files.readString(pathFileHtml);
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select(cssSelector);

        return elements.get(0).text();

    }
}
