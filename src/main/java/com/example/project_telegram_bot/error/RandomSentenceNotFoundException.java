package com.example.project_telegram_bot.error;

public class RandomSentenceNotFoundException extends RuntimeException {
    public RandomSentenceNotFoundException(String message) {
        super(message);
    }

    public RandomSentenceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
