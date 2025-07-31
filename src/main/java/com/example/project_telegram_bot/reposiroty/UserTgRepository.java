package com.example.project_telegram_bot.reposiroty;

import com.example.project_telegram_bot.entity.UserTg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTgRepository extends JpaRepository<UserTg, Long> {

    boolean existsByChatId(Long chatId);

    UserTg findByChatId(Long chatId);

    void deleteByChatId(Long chatId);
}








