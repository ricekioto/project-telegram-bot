package com.example.project_telegram_bot.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

public class SentenceResponse {
    private String content;

    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
