package com.example.project_telegram_bot.reposiroty;

import com.example.project_telegram_bot.entity.UserTg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTgRepository extends JpaRepository<UserTg, Long> {

    boolean existsByChatId(Long chatId);

    UserTg findByChatId(Long chatId);


    void deleteByChatId(Long chatId);

    List<UserTg> findAllByInterval(Byte interval);

    @Query("update UserTg u set u.interval = :interval where u.chatId = :chatId")
    @Modifying
    UserTg updateIntervalByChatId(Byte interval, Long chatId);









