package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.entity.Constants;
import com.example.project_telegram_bot.reposiroty.UserRepository;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

import static com.example.project_telegram_bot.entity.UserState.*;


public class ResponseHandler {
    private KeyboardFactory keyboardFactory;
    private UserRepository userRepository;
    private EnglishService englishService;
    private SilentSender sender;
    private Map<Long, Object> chatStates;
    private SendMessage sendMessage;


    public ResponseHandler(SilentSender silentSender,
                           DBContext db,
                           KeyboardFactory keyboardFactory,
                           UserRepository userRepository,
                           EnglishService englishService) {
        this.keyboardFactory = keyboardFactory;
        this.userRepository = userRepository;
        this.englishService = englishService;
        sender = silentSender;
        chatStates = db.getMap(Constants.CHAT_STATES);
        sendMessage = new SendMessage();
    }

    public void toStart(long chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(keyboardFactory.getSentence());
        chatStates.put(chatId, MENU);
        sender.execute(sendMessage);
    }

    public void replyToButtons(long chatId, Message message) {
        if ("/stop".equalsIgnoreCase(message.getText())) {
            stopChat(chatId);
            return;
        }
        if ("Получить сгенерированное сообщение на английском языке".equalsIgnoreCase(message.getText())) {
            getSentence(chatId, message);
            return;
        }
        switch (chatStates.get(chatId)) {
            case START -> toStart(chatId);
            case MENU -> menu(chatId, message);
            case SENTENCE -> getSentence(chatId, message);
            default -> unexpectedMessage(chatId);
        }

    }

    public void menu(long chatId, Message message) {
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(keyboardFactory.getSentence());
        chatStates.put(chatId, SENTENCE);
        sender.execute(sendMessage);
    }

    public void getSentence(long chatId, Message message) {
        sendMessage.setChatId(chatId);
        String messageText = englishService.getSentence();
        sendMessage.setText(messageText);
        chatStates.put(chatId, MENU);
        sender.execute(sendMessage);
    }

    //    @Scheduled(cron = "0 */30 * * * *")
    public void interlvalTime30(long chatId, Message message) {
        sendMessage.setChatId(chatId);
        String messageText = englishService.getSentence();
        sendMessage.setText(messageText);
        chatStates.put(chatId, MENU);
        sender.execute(sendMessage);
    }

    //@Scheduled(cron = "0 0 * * * *")
    public void interlvalTime60(long chatId, Message message) {
        sendMessage.setChatId(chatId);


    }

    private void unexpectedMessage(long chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setText("У меня нет ответа на этот случай.");
        chatStates.put(chatId, MENU);
        sender.execute(sendMessage);
    }

    public void stopChat(long chatId) {
        sendMessage.setChatId(chatId);
        chatStates.remove(chatId);
        sendMessage.setReplyMarkup(keyboardFactory.closeKeyboard());
        sender.execute(sendMessage);
    }

    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }
}
