package com.example.project_telegram_bot.reposiroty;

import com.example.project_telegram_bot.entity.UserTg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserTg, Long> {
    UserTg findUserByName(String username);

    UserTg findUserById(Long id);

    boolean existsById(Long id);

}
