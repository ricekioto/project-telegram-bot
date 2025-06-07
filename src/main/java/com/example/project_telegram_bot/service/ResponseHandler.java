package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.entity.Constants;
import com.example.project_telegram_bot.entity.UserState;
import com.example.project_telegram_bot.reposiroty.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

import static com.example.project_telegram_bot.entity.Constants.CHAT_CLOSE;
import static com.example.project_telegram_bot.entity.UserState.INTERVAL30;
import static com.example.project_telegram_bot.entity.UserState.MENU;


public class ResponseHandler {
    private KeyboardFactory keyboardFactory;
    private UserRepository userRepository;
    private EnglishService englishService;
    private SilentSender sender;
    private Map<Long, UserState> chatStates;
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
//        if (!userRepository.existsByChatId(chatId)) {
//            userRepository.save(chatId);
//        }
        sendMessage.setChatId(chatId);
        chatStates.put(chatId, MENU);
    }

    public void replyToButtons(long chatId, Message message) {
        closeKeyboard(chatId);
        if (message.getText().equalsIgnoreCase("/stop")) {
            stopChat(chatId);
        }
        if (message.getText().equalsIgnoreCase("Получить сгенерированное сообщение на английском языке")) {
            interlvalTime30(chatId, message);
        } else {
            switch (chatStates.get(chatId)) {
                case START -> toStart(chatId);
                case MENU -> menu(chatId, message);
                case INTERVAL30 -> interlvalTime30(chatId, message);
//            case INTERVAL60 ->
                default -> unexpectedMessage(chatId);
            }
        }
    }

    public void menu(long chatId, Message message) {
        sendMessage.setChatId(chatId);
        intervalTimeKeyboard(chatId);
        chatStates.put(chatId, INTERVAL30);
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

    @Scheduled(cron = "0 0 * * * *")
    public void interlvalTime60(long chatId, Message message) {
        sendMessage.setChatId(chatId);


    }

    private void unexpectedMessage(long chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setText("У меня нет ответа на этот случай.");
        chatStates.put(chatId, MENU);
        sender.execute(sendMessage);
    }

    public void closeKeyboard(long chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(keyboardFactory.closeKeyboard());
        sender.execute(sendMessage);
    }

    public void intervalTimeKeyboard(long chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(keyboardFactory.interlvalTime());
        sender.execute(sendMessage);
    }

    private void stopChat(long chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setText(CHAT_CLOSE);
        chatStates.remove(chatId);
        sendMessage.setReplyMarkup(keyboardFactory.closeKeyboard());
        sender.execute(sendMessage);
    }

    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }
}
