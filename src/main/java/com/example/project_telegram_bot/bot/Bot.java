package com.example.project_telegram_bot.bot;

import com.example.project_telegram_bot.entity.Constants;
import com.example.project_telegram_bot.reposiroty.UserTgRepository;
import com.example.project_telegram_bot.service.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.BiConsumer;

import static com.example.project_telegram_bot.entity.Constants.CHAT_STATES;
import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Component
public class Bot extends AbilityBot {
    private final ResponseHandlerService responseHandlerService;
    private final UserTgRepository userTgRepository;

    public Bot(Environment env, UserTgRepository userTgRepository,
               KeyboardFactory keyboardFactory, EnglishRandomService englishRandomService,
               TranslatorService translatorService, RequestService requestService) {
        super(env.getProperty("bot.token"), "bot.name");
        this.responseHandlerService = new ResponseHandlerService(silent,
                db, keyboardFactory, userTgRepository, englishRandomService, translatorService, requestService);
        this.userTgRepository = userTgRepository;
    }

    public Ability startBot() {
        return Ability
                .builder()
                .name("start")
                .info(Constants.START_DESCRIPTION)
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> responseHandlerService.toStart(ctx.chatId()))
                .build();
    }

    public Ability resetCommand() {
        return Ability.builder()
                .name("reset")
                .info("Resets the bot state for this chat only.")
                .locality(Locality.ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    long chatId = ctx.chatId();
                    db.getMap(CHAT_STATES).remove(chatId);
                    silent.send("Состояние бота сброшено!", chatId);
                })
                .build();
    }

    public Reply replyToButtons() {
        BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) -> responseHandlerService.replyToButtons(getChatId(upd), upd.getMessage());
        return Reply.of(action, Flag.TEXT, upd -> responseHandlerService.userIsActive(getChatId(upd)));
    }

    @Override
    public long creatorId() {
        return 1L;
    }

    public ResponseHandlerService getResponseHandler() {
        return responseHandlerService;
    }

    public UserTgRepository getUserTgRepository() {
        return userTgRepository;
    }
}
