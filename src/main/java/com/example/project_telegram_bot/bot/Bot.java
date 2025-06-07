package com.example.project_telegram_bot.bot;

import com.example.project_telegram_bot.entity.Constants;
import com.example.project_telegram_bot.entity.UserTg;
import com.example.project_telegram_bot.reposiroty.UserRepository;
import com.example.project_telegram_bot.service.EnglishService;
import com.example.project_telegram_bot.service.KeyboardFactory;
import com.example.project_telegram_bot.service.ResponseHandler;
import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.fullName;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Getter
@Component
public class Bot extends AbilityBot {
    private final ResponseHandler responseHandler;
    private final UserRepository userRepository;
    private static final String USER_CONTEXT = "userContext";

    public Bot(Environment env, UserRepository userRepository,
               KeyboardFactory keyboardFactory, EnglishService englishService) {
        super(env.getProperty("bot.token"), "bot.name");
        this.responseHandler = new ResponseHandler(silent, db, keyboardFactory, userRepository, englishService);
        this.userRepository = userRepository;
    }

    public Ability resetAllCommand() {
        return Ability
                .builder()
                .name("resetall")
                .info("Resets the bot state for ALL users. Only for admins! USE WITH EXTREME CAUTION! THIS WILL ERASE ALL USER DATA!")
                .locality(Locality.ALL)
                .privacy(Privacy.ADMIN) // Только для администраторов
                .action(ctx -> {
                    long chatId = ctx.chatId();
                    if (!isAdmin(chatId)) { // Проверяем, что пользователь - администратор
                        silent.send("У вас нет прав для выполнения этой команды!", chatId);
                        return;
                    }

                    Map<Long, Object> userContextMap = db.getMap(USER_CONTEXT);
                    // Получаем копию ключей, чтобы избежать ConcurrentModificationException
                    Set<Long> chatIds = new HashSet<>(userContextMap.keySet());

                    for (Long id : chatIds) {
                        userContextMap.remove(id); // Удаляем записи по chatId
                        // Если нужно, можно добавить логирование удаления chatId
                    }

                    silent.send("Состояние бота сброшено для ВСЕХ пользователей! Все данные пользователей удалены!", chatId);
                })
                .build();
    }

    public Ability startBot() {
        return Ability
                .builder()
                .name("start")
                .info(Constants.START_DESCRIPTION)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> responseHandler.toStart(ctx.chatId()))
                .build();
    }

    public Reply replyToButtons() {
        BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) -> responseHandler.replyToButtons(getChatId(upd), upd.getMessage());
        return Reply.of(action, Flag.TEXT, upd -> responseHandler.userIsActive(getChatId(upd)));
    }

    @Override
    public long creatorId() {
        return 1L;
    }
}
