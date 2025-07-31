package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.entity.UserTg;
import com.example.project_telegram_bot.error.UserTgException;
import com.example.project_telegram_bot.reposiroty.UserTgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserTgService {
    private final UserTgRepository userTgRepository;

    @Transactional
    public boolean existsByChatId(Long chatId) throws UserTgException {
        return userTgRepository.existsByChatId(chatId);
    }

    @Transactional
    public UserTg findByChatId(Long chatId) throws UserTgException {
        return userTgRepository.findByChatId(chatId);
    }

    @Transactional
    public UserTg save(UserTg userTg) throws UserTgException {
        return userTgRepository.save(userTg);
    }

    @Transactional
    public void deleteByChatId(Long chatId) throws UserTgException {
        userTgRepository.deleteByChatId(chatId);
    }
}








