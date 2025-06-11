package com.example.project_telegram_bot.error;

public class RequestServiceException extends RuntimeException {

  public RequestServiceException(String message) {
    super(message);
  }

  public RequestServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
