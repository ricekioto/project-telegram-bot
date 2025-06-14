package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.entity.UserTg;
import com.example.project_telegram_bot.error.UserTgException;
import com.example.project_telegram_bot.reposiroty.UserTgRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserTgService {
    private static final Logger logger = LoggerFactory.getLogger(UserTgException.class);

    private UserTgRepository userTgRepository;

    public UserTgService(UserTgRepository userTgRepository) {
        this.userTgRepository = userTgRepository;
    }

    @Transactional
    public UserTg getById(Long id) throws UserTgException {
        if (userTgRepository.existsById(id) && Objects.isNull(id)) {
            logger.warn("id равен null или сущности с таким id не существует");
            throw new UserTgException("Id is null");
        }
        return userTgRepository.findUserById(id);
    }
}
