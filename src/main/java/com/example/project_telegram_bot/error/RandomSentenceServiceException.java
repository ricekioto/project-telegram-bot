package com.example.project_telegram_bot.error;

public class RandomSentenceServiceException extends RuntimeException {
    public RandomSentenceServiceException(String message) {
        super(message);
    }

    public RandomSentenceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
