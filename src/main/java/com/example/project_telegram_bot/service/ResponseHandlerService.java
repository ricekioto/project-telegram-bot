package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.entity.Constants;
import com.example.project_telegram_bot.reposiroty.UserTgRepository;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;

import static com.example.project_telegram_bot.entity.Constants.ANOTHER_ANSWER;
import static com.example.project_telegram_bot.entity.Constants.CHAT_CLOSE;
import static com.example.project_telegram_bot.enums.UserState.MENU;


public class ResponseHandlerService {
    private final String URL_RANDOM_SENTENCE_CONTROLLER = "http://localhost:8081/api/random/sentence";

    private KeyboardFactoryService keyboardFactoryService;
    private UserTgRepository userTgRepository;
    private EnglishRandomService englishRandomService;
    private TranslatorService translatorService;
    private RequestService requestService;
    private SilentSender sender;
    private Map<Long, Object> chatStates;
    private List<Long> every10Seconds;
    private List<Long> every30Minutes;
    private List<Long> every60Minutes;


    public ResponseHandlerService(SilentSender silentSender,
                                  DBContext db,
                                  KeyboardFactoryService keyboardFactoryService,
                                  UserTgRepository userTgRepository,
                                  EnglishRandomService englishRandomService,
                                  TranslatorService translatorService,
                                  RequestService requestService) {
        this.keyboardFactoryService = keyboardFactoryService;
        this.userTgRepository = userTgRepository;
        this.englishRandomService = englishRandomService;
        this.translatorService = translatorService;
        this.requestService = requestService;
        sender = silentSender;
        chatStates = db.getMap(Constants.CHAT_STATES);
        every10Seconds = db.getList(Constants.CHATS_EVERY_10_SECONDS);
        every30Minutes = db.getList(Constants.CHATS_EVERY_30_MINUTES);
        every60Minutes = db.getList(Constants.CHATS_EVERY_60_MINUTES);
    }

    public void toStart(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Нажми на кнопку \"Получить\" для получения " +
                "сгенерированного предложения на английском языке.\n" +
                "Или можешь выбрать время c которым будет отправляться сообщение.");
        sendMessage.setReplyMarkup(keyboardFactoryService.getSentenceAndStop());
        chatStates.put(chatId, MENU);
        sender.execute(sendMessage);

    }

    public void replyToButtons(long chatId, Message message) {
        SendMessage sendMessage = new SendMessage();
        switch (message.getText()) {
            case "Остановить бота" -> stopChat(chatId);
            case "Получить" -> getSentence(chatId);
            case "5 минут" -> {
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
        String messageText = requestService.get(URL_RANDOM_SENTENCE_CONTROLLER);
//        String translatedText = translatorService.getTranslatedText(messageText);
//        translatedText = escapeMarkdownV2(translatedText);
//        String returnText = messageText + "\nПеревод.\n*|| example:" + translatedText + " ||*";
        sendMessage.setText(messageText);
//        sendMessage.setParseMode("MARKDOWNV2");
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

    public SilentSender getSender() {
        return sender;
    }

    public List<Long> getEvery10Seconds() {
        return every10Seconds;
    }

    public List<Long> getEvery30Minutes() {
        return every30Minutes;
    }

    public List<Long> getEvery60Minutes() {
        return every60Minutes;
    }

    public RequestService getRequestService() {
        return requestService;
    }

    public String getURL_RANDOM_SENTENCE_CONTROLLER() {
        return URL_RANDOM_SENTENCE_CONTROLLER;
    }
}
