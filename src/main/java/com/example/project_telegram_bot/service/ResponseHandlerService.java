package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.entity.Constants;
import com.example.project_telegram_bot.reposiroty.UserTgRepository;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${url.english.random-controller}")
    private String urlRandomSentenceController;

    private KeyboardFactory keyboardFactory;
    private UserTgRepository userTgRepository;
    private EnglishRandomService englishRandomService;
    private TranslatorService translatorService;
    private RequestService requestService;
    private SilentSender sender;
    private Map<Long, Object> chatStates;
    private List<Long> every10Seconds;
    private List<Long> every30Minutes;
    private List<Long> every60Minutes;
    private SendMessage sendMessage;


    public ResponseHandlerService(SilentSender silentSender,
                                  DBContext db,
                                  KeyboardFactory keyboardFactory,
                                  UserTgRepository userTgRepository,
                                  EnglishRandomService englishRandomService,
                                  TranslatorService translatorService,
                                  RequestService requestService) {
        this.keyboardFactory = keyboardFactory;
        this.userTgRepository = userTgRepository;
        this.englishRandomService = englishRandomService;
        this.translatorService = translatorService;
        this.requestService = requestService;
        sender = silentSender;
        chatStates = db.getMap(Constants.CHAT_STATES);
        every10Seconds = db.getList(Constants.CHATS_EVERY_10_SECONDS);
        every30Minutes = db.getList(Constants.CHATS_EVERY_30_MINUTES);
        every60Minutes = db.getList(Constants.CHATS_EVERY_60_MINUTES);
        sendMessage = new SendMessage();
    }

    public void toStart(long chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setText("Нажми на кнопку \"Получить\" для получения " +
                "сгенерированного предложения на английском языке.\n" +
                "Или можешь выбрать время, которое будет отправляться с выбранным периодом времени.");
        sendMessage.setReplyMarkup(keyboardFactory.getSentenceAndStop());
        chatStates.put(chatId, MENU);
        sender.execute(sendMessage);

    }

    public void replyToButtons(long chatId, Message message) {
        switch (message.getText()) {
            case "Остановить бота" -> stopChat(chatId);
            case "Получить" -> getSentence(chatId);
            case "10 секунд" -> {
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
        sendMessage.setChatId(chatId);
        String messageText = requestService.getEntity(urlRandomSentenceController);
//        String translatedText = translatorService.getTranslatedText(messageText);
//        translatedText = escapeMarkdownV2(translatedText);
//        String returnText = messageText + "\nПеревод.\n*|| example:" + translatedText + " ||*";
        sendMessage.setText(messageText);
//        sendMessage.setParseMode("MARKDOWNV2");
        sender.execute(sendMessage);
    }

    private void unexpectedMessage(long chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setText(ANOTHER_ANSWER);
        sender.execute(sendMessage);
    }

    public void stopChat(long chatId) {
        sendMessage.setChatId(chatId);
        chatStates.remove(chatId);
        every10Seconds.remove(chatId);
        every30Minutes.remove(chatId);
        every60Minutes.remove(chatId);
        sendMessage.setText(CHAT_CLOSE);
        sendMessage.setReplyMarkup(keyboardFactory.toStart());
        sender.execute(sendMessage);
    }

    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }

    public KeyboardFactory getKeyboardFactory() {
        return keyboardFactory;
    }

    public UserTgRepository getUserTgRepository() {
        return userTgRepository;
    }

    public EnglishRandomService getEnglishService() {
        return englishRandomService;
    }

    public SilentSender getSender() {
        return sender;
    }

    public Map<Long, Object> getChatStates() {
        return chatStates;
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

    public SendMessage getSendMessage() {
        return sendMessage;
    }
}
