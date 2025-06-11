package com.example.project_telegram_bot.error;

public class ParsingHtmlException extends RuntimeException {
    public ParsingHtmlException(String message) {
        super(message);
    }

    public ParsingHtmlException(String message, Throwable cause) {
        super(message, cause);
    }
}
