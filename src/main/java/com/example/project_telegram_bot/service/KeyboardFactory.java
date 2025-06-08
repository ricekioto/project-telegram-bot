package com.example.project_telegram_bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Service
public class KeyboardFactory {
    public ReplyKeyboard toStart() {
        KeyboardRow row = new KeyboardRow();
        row.add("/start");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public ReplyKeyboard getSentenceAndStop() {
        KeyboardRow row = new KeyboardRow();
        row.add("Получить");
        row.add("10 секунд");
        row.add("30 минут");
        row.add("60 минут");
        row.add("Не отправлять по расписанию");
        row.add("Остановить бота");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public ReplyKeyboard interlvalTime() {
        KeyboardRow row = new KeyboardRow();
        row.add("30");
        row.add("60");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public ReplyKeyboard closeKeyboard() {
        return new ReplyKeyboardRemove(true);
    }

}
