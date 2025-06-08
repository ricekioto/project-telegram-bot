package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.entity.Constants;
import com.example.project_telegram_bot.reposiroty.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;

import static com.example.project_telegram_bot.entity.UserState.*;


public class ResponseHandler {
    private KeyboardFactory keyboardFactory;
    private UserRepository userRepository;
    private EnglishService englishService;
    private SilentSender sender;
    private Map<Long, Object> chatStates;
    private List<Long> every10Seconds;
    private List<Long> every30Minutes;
    private List<Long> every60Minutes;
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
        String messageText = englishService.getSentence();
        sendMessage.setText(messageText);
        sender.execute(sendMessage);
    }

    @Async
    @Scheduled(cron = "*/10 * * * * *")
    public void interlvalTime10Second() {
        try {
            for (Long instance : every10Seconds) {
                sendMessage.setChatId(instance);
                String messageText = englishService.getSentence();
                sendMessage.setText(messageText);
                sender.execute(sendMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 */30 * * * *")
    public void interlvalTime30Minute() {
        for (Long instance : every30Minutes) {
            sendMessage.setChatId(instance);
            String messageText = englishService.getSentence();
            sendMessage.setText(messageText);
            sender.execute(sendMessage);
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void interlvalTime60Minute() {
        for (Long instance : every60Minutes) {
            sendMessage.setChatId(instance);
            String messageText = englishService.getSentence();
            sendMessage.setText(messageText);
            sender.execute(sendMessage);
        }
    }


    private void unexpectedMessage(long chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setText("У меня нет ответа на этот случай. Выбери из предложенных вариантов.");
        sender.execute(sendMessage);
    }

    public void stopChat(long chatId) {
        sendMessage.setChatId(chatId);
        chatStates.remove(chatId);
        every10Seconds.remove(chatId);
        every30Minutes.remove(chatId);
        every60Minutes.remove(chatId);
        chatStates.clear();
        every10Seconds.clear();
        every30Minutes.clear();
        every60Minutes.clear();
        sendMessage.setText("Бот остановлен");
        sendMessage.setReplyMarkup(keyboardFactory.toStart());
        sender.execute(sendMessage);
    }

    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }
}
