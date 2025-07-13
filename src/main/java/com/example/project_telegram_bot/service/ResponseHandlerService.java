package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.entity.Constants;
import com.example.project_telegram_bot.entity.UserTg;
import lombok.Getter;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;

import static com.example.project_telegram_bot.entity.Constants.*;
import static com.example.project_telegram_bot.enums.UserState.MENU;
import static com.example.project_telegram_bot.service.MarkdownV2Service.escapeMarkdownV2;


@Getter
public class ResponseHandlerService {
    private BuildingUrlService buildingUrlService;
    private KeyboardFactoryService keyboardFactoryService;
    private UserTgService userTgService;
    private RequestService requestService;
    private SilentSender sender;
    private Map<Long, Object> chatStates;
    private List<Long> every10Seconds;
    private List<Long> every30Minutes;
    private List<Long> every60Minutes;

    public ResponseHandlerService(SilentSender silentSender,
                                  DBContext db,
                                  KeyboardFactoryService keyboardFactoryService,
                                  UserTgService userTgService,
                                  RequestService requestService, BuildingUrlService buildingUrlService) {
        this.keyboardFactoryService = keyboardFactoryService;
        this.userTgService = userTgService;
        this.requestService = requestService;
        this.buildingUrlService = buildingUrlService;
        sender = silentSender;
        chatStates = db.getMap(Constants.CHAT_STATES);
        every10Seconds = db.getList(Constants.CHATS_EVERY_10_SECONDS);
        every30Minutes = db.getList(Constants.CHATS_EVERY_30_MINUTES);
        every60Minutes = db.getList(Constants.CHATS_EVERY_60_MINUTES);
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
        chatStates.put(chatId, MENU);
        sender.execute(sendMessage);
    }

    public void replyToButtons(long chatId, Message message) {
        SendMessage sendMessage = new SendMessage();
        switch (message.getText()) {
            case "Остановить бота" -> stopChat(chatId);
            case "Получить" -> getSentence(chatId);
            case "15 минут" -> {
                if (!every10Seconds.contains(chatId)) {
                    every10Seconds.add(chatId);
                }
                every30Minutes.remove(chatId);
                every60Minutes.remove(chatId);
                sendMessage.setChatId(chatId);
                sendMessage.setText("Настройки изменены");
                sender.execute(sendMessage);
            }
            case "30 минут" -> {
                if (!every30Minutes.contains(chatId)) {
                    every30Minutes.add(chatId);
                }
                every60Minutes.remove(chatId);
                every10Seconds.remove(chatId);
                sendMessage.setChatId(chatId);
                sendMessage.setText("Настройки изменены");
                sender.execute(sendMessage);
            }
            case "60 минут" -> {
                if (!every60Minutes.contains(chatId)) {
                    every60Minutes.add(chatId);
                }
                every30Minutes.remove(chatId);
                every10Seconds.remove(chatId);
                sendMessage.setChatId(chatId);
                sendMessage.setText("Настройки изменены");
                sender.execute(sendMessage);
            }
            case "Не отправлять по расписанию" -> {
                every10Seconds.remove(chatId);
                every30Minutes.remove(chatId);
                every60Minutes.remove(chatId);
                sendMessage.setChatId(chatId);
                sendMessage.setText("Настройки изменены");
                sender.execute(sendMessage);
            }
            default -> unexpectedMessage(chatId);
        }
    }

    public void getSentence(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        String randomEnglishControllerUrl = buildingUrlService.getGeneratorControllerUrl();
        String messageText = requestService.get(randomEnglishControllerUrl);
        String translationControllerUrl = buildingUrlService.getTranslationControllerUrl(messageText);
        String translatedText = requestService.get(translationControllerUrl);

        translatedText = escapeMarkdownV2(translatedText);
        messageText = escapeMarkdownV2(messageText);
        String returnText = messageText + "\n\n||" + translatedText + "||";
        sendMessage.setText(returnText);
        sendMessage.setParseMode("MARKDOWNV2");
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
        chatStates.remove(chatId);
        every10Seconds.remove(chatId);
        every30Minutes.remove(chatId);
        every60Minutes.remove(chatId);
        sendMessage.setText(CHAT_CLOSE);
        sendMessage.setReplyMarkup(keyboardFactoryService.toStart());
        sender.execute(sendMessage);
    }

    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }
}
