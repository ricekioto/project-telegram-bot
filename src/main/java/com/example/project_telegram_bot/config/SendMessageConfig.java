package com.example.project_telegram_bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Configuration
public class SendMessageConfig {

    @Bean
    public SendMessage sendMessage() {
        return new SendMessage();
    }
}
