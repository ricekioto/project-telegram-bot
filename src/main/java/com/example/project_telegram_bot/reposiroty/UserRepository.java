package com.example.project_telegram_bot.reposiroty;

import com.example.project_telegram_bot.entity.UserTg;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserTg, Long> {

    UserTg findUserById(Long id);

    boolean existsByChatId(Long chatId);
}
