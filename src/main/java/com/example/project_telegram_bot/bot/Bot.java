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
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.objects.Update;

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

    public Bot(Environment env, UserRepository userRepository,
               KeyboardFactory keyboardFactory, EnglishService englishService) {
        super(env.getProperty("bot.token"), "bot.name");
        this.responseHandler = new ResponseHandler(silent, db, keyboardFactory, userRepository, englishService);
        this.userRepository = userRepository;
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
