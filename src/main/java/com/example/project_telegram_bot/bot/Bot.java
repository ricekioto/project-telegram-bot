package com.example.project_telegram_bot.bot;

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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
               KeyboardFactoryService keyboardFactoryService, EnglishRandomService englishRandomService,
               TranslatorService translatorService, RequestService requestService, SendMessage sendMessage) {
        super(env.getProperty("bot.token"), "bot.name");
        this.responseHandlerService = new ResponseHandlerService(silent, db,
                keyboardFactoryService, userTgRepository,
                englishRandomService, translatorService,
                requestService, sendMessage);
        this.userTgRepository = userTgRepository;
    }

    public Ability startBot() {
        return Ability
                .builder()
                .name("start")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> responseHandlerService.toStart(ctx.chatId()))
                .build();
    }

    public Ability resetCommand() {
        return Ability.builder()
                .name("reset")
                .locality(Locality.ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    long chatId = ctx.chatId();
                    db.getMap(CHAT_STATES).remove(chatId);
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
