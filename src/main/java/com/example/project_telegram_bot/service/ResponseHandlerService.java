package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.bot.Bot;
import com.example.project_telegram_bot.entity.Constants;
import com.example.project_telegram_bot.entity.UserTg;
import lombok.Getter;
import org.springframework.stereotype.Service;
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
@Service
public class ResponseHandlerService {
    private BuildingUrlService buildingUrlService;
    private KeyboardFactoryService keyboardFactoryService;
    private UserTgService userTgService;
    private RequestService requestService;
    private SilentSender sender;

    public ResponseHandlerService(Bot bot,
                                  KeyboardFactoryService keyboardFactoryService,
                                  UserTgService userTgService,
                                  RequestService requestService, BuildingUrlService buildingUrlService) {
        this.keyboardFactoryService = keyboardFactoryService;
        this.userTgService = userTgService;
        this.requestService = requestService;
        this.buildingUrlService = buildingUrlService;
        sender = bot.silent();
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
        switch (message.getText()) {
            case "Остановить бота" -> stopChat(chatId);
            case "Получить" -> getSentence(chatId);
            case "15 минут" -> {

                sendMessage.setChatId(chatId);
                sendMessage.setText("Настройки изменены");
                sender.execute(sendMessage);
            }
            case "30 минут" -> {

                sendMessage.setChatId(chatId);
                sendMessage.setText("Настройки изменены");
                sender.execute(sendMessage);
            }
            case "60 минут" -> {

                sendMessage.setChatId(chatId);
                sendMessage.setText("Настройки изменены");
                sender.execute(sendMessage);
            }
            case "Не отправлять по расписанию" -> {

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
        sendMessage.setText(CHAT_CLOSE);
        sendMessage.setReplyMarkup(keyboardFactoryService.toStart());
        sender.execute(sendMessage);
    }

    public boolean userIsActive(Long chatId) {
        return userTgService.existsByChatId(chatId);
    }
}






