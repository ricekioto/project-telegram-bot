package com.example.project_telegram_bot.error;

public class ElementNotFoundException extends ParsingHtmlException {

    public ElementNotFoundException(String message) {
        super(message);
    }

    public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
