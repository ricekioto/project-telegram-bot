package com.example.project_telegram_bot.reposiroty;

import com.example.project_telegram_bot.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByName(String username);

    User findUserById(Long id);

}
