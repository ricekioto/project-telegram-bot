package com.example.project_telegram_bot.reposiroty;

import com.example.project_telegram_bot.entity.UserTg;
import com.example.project_telegram_bot.entity.enums.Interval;
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

    List<UserTg> findAllByInterval(Interval interval);

    void deleteUserTgsById(Long id);

    @Modifying
    @Query("update UserTg u set u.interval = :interval where u.chatId = :chatId")
    UserTg updateIntervalByChatId(Interval interval, Long chatId);
}
