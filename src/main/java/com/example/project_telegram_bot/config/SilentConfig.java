package com.example.project_telegram_bot.config;

import com.example.project_telegram_bot.service.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.abilitybots.api.sender.SilentSender;

@Configuration
@RequiredArgsConstructor
public class SilentConfig {

    @Bean
    public SilentSender silentSender(BotService botService) {
        return botService.silent();
    }
}