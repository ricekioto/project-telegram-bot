package com.example.project_telegram_bot.controller;

import com.example.project_telegram_bot.service.EnglishRandomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/random")
public class RandomSentenceController {

    private final EnglishRandomService englishRandomService;

    public RandomSentenceController(EnglishRandomService englishRandomService) {
        this.englishRandomService = englishRandomService;
    }

    @GetMapping("/sentence")
    public ResponseEntity<String> getRandomSentenceEndpoint() {
        String sentence = englishRandomService.getSentence();
        if (sentence != null) {
            return ResponseEntity.ok(sentence);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Не удалось получить случайное предложение.");
        }
    }
}
