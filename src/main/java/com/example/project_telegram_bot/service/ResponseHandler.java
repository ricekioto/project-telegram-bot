package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.entity.Constants;
import com.example.project_telegram_bot.entity.UserState;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

import static com.example.project_telegram_bot.entity.Constants.CHAT_CLOSE;
import static com.example.project_telegram_bot.entity.UserState.MENU;


public class ResponseHandler {
    private final SilentSender sender;
    private Map<Long, UserState> chatStates;
    private SendMessage sendMessage;


    public ResponseHandler(SilentSender silentSender, DBContext db) {
        this.sender = silentSender;
        chatStates = db.getMap(Constants.CHAT_STATES);
        sendMessage = new SendMessage();
    }

    public void toStart(long chatId) {
        sendMessage.setChatId(chatId);
        sender.execute(sendMessage);
        chatStates.put(chatId, MENU);
    }

    public void replyToButtons(long chatId, Message message) {
        if (message.getText().equalsIgnoreCase("/stop")) {
            stopChat(chatId);
        }
        switch (chatStates.get(chatId)) {
            case START -> toStart(chatId);
            case MENU -> menu(chatId, message);
            default -> unexpectedMessage(chatId);
        }
    }

    public void menu(long chatId, Message message) {
        sendMessage.setChatId(chatId);
        sendMessage.setText("Выбери интервал времени, с которым будет отправляться сообщение");
    }

    private void unexpectedMessage(long chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setText("У меня нет ответа на этот случай.");
        chatStates.put(chatId, MENU);
        sender.execute(sendMessage);
    }

    private void stopChat(long chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setText(CHAT_CLOSE);
        chatStates.remove(chatId);
        sendMessage.setReplyMarkup(KeyboardFactory.toStart());
        sender.execute(sendMessage);

    }

//    private void replyToOrder(long chatId, Message message) {
//        sendMessage.setChatId(chatId);
//        if ("yes".equalsIgnoreCase(message.getText())) {
//            sendMessage.setText("спс");
//            sendMessage.setReplyMarkup(KeyboardFactory.getPizzaOrDrinkKeyboard());
//            sender.execute(sendMessage);
//            chatStates.put(chatId, AWAITING_NAME);
//        } else if ("no".equalsIgnoreCase(message.getText())) {
//            stopChat(chatId);
//        } else {
//            sendMessage.setText("Пожалуйста, выбери что-то");
//            sendMessage.setReplyMarkup(KeyboardFactory.getYesOrNo());
//            sender.execute(sendMessage);
//        }
//    }
//
//    private void replyToPizzaToppings(long chatId, Message message) {
//        if ("margherita".equalsIgnoreCase(message.getText())) {
//            promptWithKeyboardForState(chatId, "выбери что-то снова",
//                    KeyboardFactory.getYesOrNo(), );
//        } else if ("pepperoni".equalsIgnoreCase(message.getText())) {
//            promptWithKeyboardForState(chatId, "тут короч тож",
//                    KeyboardFactory.getPizzaToppingsKeyboard(), );
//        } else {
//            SendMessage sendMessage = new SendMessage();
//            sendMessage.setChatId(chatId);
//            sendMessage.setText("ага " + message.getText() + " выбери еще");
//            sendMessage.setReplyMarkup(KeyboardFactory.getPizzaToppingsKeyboard());
//            sender.execute(sendMessage);
//        }
//    }
//
//    private void promptWithKeyboardForState(long chatId, String text, ReplyKeyboard YesOrNo, UserState awaitingReorder) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(text);
//        sendMessage.setReplyMarkup(YesOrNo);
//        sender.execute(sendMessage);
//        chatStates.put(chatId, awaitingReorder);
//    }
//
//    private void replyToFoodDrinkSelection(long chatId, Message message) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(chatId);
//        if ("drink".equalsIgnoreCase(message.getText())) {
//            sendMessage.setText("тут такого нет");
//            sendMessage.setReplyMarkup(KeyboardFactory.getPizzaOrDrinkKeyboard());
//            sender.execute(sendMessage);
//        } else if ("pizza".equalsIgnoreCase(message.getText())) {
//            sendMessage.setText("и тут тоже");
//            sendMessage.setReplyMarkup(KeyboardFactory.getPizzaToppingsKeyboard());
//            sender.execute(sendMessage);
//            chatStates.put(chatId, AWAITING_NAME);
//        } else {
//            sendMessage.setText("ага " + message.getText() + ". выбери еще что-то");
//            sendMessage.setReplyMarkup(KeyboardFactory.getPizzaOrDrinkKeyboard());
//            sender.execute(sendMessage);
//        }
//    }
//
//    private void replyToName(long chatId, Message message) {
//        SendMessage sendMessage = new SendMessage();
//    }
//
    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }
}
