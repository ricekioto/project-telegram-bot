package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.entity.Constants;
import com.example.project_telegram_bot.entity.UserTg;
import com.example.project_telegram_bot.entity.enums.Interval;
import com.example.project_telegram_bot.kafka.SentenceProducer;
import com.example.project_telegram_bot.kafka.TranslationProducer;
import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.UUID;
import java.util.function.BiConsumer;

import static com.example.project_telegram_bot.entity.Constants.*;
import static com.example.project_telegram_bot.service.MarkdownV2Service.escapeMarkdownV2;
import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Getter
@Component
public class BotService extends AbilityBot {
    private UserTgService userTgService;
    private KeyboardFactoryService keyboardFactoryService;
    private RequestService requestService;
    private BuildingUrlService buildingUrlService;
    private SilentSender sender;
    private SentenceProducer sentenceProducer;
    private TranslationProducer translationProducer;
    private RequestStateService requestStateService;

    public BotService(Environment env, UserTgService userTgService,
                      KeyboardFactoryService keyboardFactoryService,
                      BuildingUrlService buildingUrlService,
                      RequestService requestService,
                      SentenceProducer sentenceProducer,
                      TranslationProducer translationProducer,
                      RequestStateService requestStateService) {
        super(env.getProperty("bot.token"), "bot.name");
        this.userTgService = userTgService;
        this.keyboardFactoryService = keyboardFactoryService;
        this.buildingUrlService = buildingUrlService;
        this.requestService = requestService;
        this.sender = silent();
        this.sentenceProducer = sentenceProducer;
        this.translationProducer = translationProducer;
        this.requestStateService = requestStateService;
    }

    public Ability startBot() {
        return Ability.builder()
                .name("start")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> toStart(ctx.chatId())).build();
    }

    public Reply replyToButtons() {
        BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) -> replyToButtons(getChatId(upd), upd.getMessage());
        return Reply.of(action, Flag.TEXT, upd -> userIsActive(getChatId(upd)));
    }

    public void toStart(long chatId) {
        if (!userTgService.existsByChatId(chatId)) {
            userTgService.save(UserTg.builder()
                    .chatId(chatId)
                    .build());
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(STARTED_MESSAGE);
        sendMessage.setReplyMarkup(keyboardFactoryService.getSentenceAndStop());
        sender.execute(sendMessage);
    }

    public void replyToButtons(long chatId, Message message) {
        SendMessage sendMessage = new SendMessage();
        UserTg user = userTgService.findByChatId(chatId);
        switch (message.getText()) {
            case "Остановить бота" -> stopChat(chatId);
            case "Получить" -> getSentence(chatId);
            case "15 минут" -> {
                if (!(user.getInterval() == Interval.INTERVAL15))
                    userTgService.updateIntervalByChatId(Interval.INTERVAL15, chatId);
                sendMessage.setChatId(chatId);
                sendMessage.setText("Настройки изменены");
                sender.execute(sendMessage);
            }
            case "30 минут" -> {
                if (!(user.getInterval() == Interval.INTERVAL30))
                    userTgService.updateIntervalByChatId(Interval.INTERVAL30, chatId);
                sendMessage.setChatId(chatId);
                sendMessage.setText("Настройки изменены");
                sender.execute(sendMessage);
            }
            case "60 минут" -> {
                if (!(user.getInterval() == Interval.INTERVAL30))
                    userTgService.updateIntervalByChatId(Interval.INTERVAL60, chatId);
                sendMessage.setChatId(chatId);
                sendMessage.setText("Настройки изменены");
                sender.execute(sendMessage);
            }
            case "Не отправлять по расписанию" -> {
                if (!(user.getInterval() == Interval.NOINTERVAL))
                    userTgService.updateIntervalByChatId(Interval.NOINTERVAL, chatId);
                sendMessage.setChatId(chatId);
                sendMessage.setText("Настройки изменены");
                sender.execute(sendMessage);
            }
            default -> unexpectedMessage(chatId);
        }
    }

    public void getSentence(long chatId) {
        UUID requestId = UUID.randomUUID();
        sentenceProducer.requestNewSentence(chatId);
    }

    public String getFullMessage(String generateText, String translatedText, long chatId) {
        translatedText = escapeMarkdownV2(translatedText);
        generateText = escapeMarkdownV2(generateText);
        String returnText = generateText + "\n\n||" + translatedText + "||";
        return returnText;
    }

    public void sendMessage(String message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        sendMessage.setText(message);
        sendMessage.setParseMode(Constants.MARKDOWNV2);
        sender.execute(sendMessage);
    }

    private void unexpectedMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setText(ANOTHER_ANSWER);
        sendMessage.setReplyMarkup(keyboardFactoryService.getSentenceAndStop());
        sender.execute(sendMessage);
    }

    public void stopChat(long chatId) {
        if (userTgService.existsByChatId(chatId)) {
            userTgService.deleteByChatId(chatId);
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(CHAT_CLOSE);
        sendMessage.setReplyMarkup(keyboardFactoryService.toStart());
        sender.execute(sendMessage);
    }

    public boolean userIsActive(Long chatId) {
        return userTgService.existsByChatId(chatId);
    }

    @Override
    public long creatorId() {
        return 1L;
    }
}