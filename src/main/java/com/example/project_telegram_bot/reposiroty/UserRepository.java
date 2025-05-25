package com.example.project_telegram_bot.reposiroty;

import com.example.project_telegram_bot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByName(String username);

    User findUserById(Long id);

}
