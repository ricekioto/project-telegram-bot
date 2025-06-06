package com.example.project_telegram_bot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class KeyboardFactory {
    public static ReplyKeyboard toStart() {
        KeyboardRow row = new KeyboardRow();
        row.add("Начать");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public static ReplyKeyboard interlvalTime() {
        KeyboardRow row = new KeyboardRow();
        row.add("30");
        row.add("60");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public static ReplyKeyboard closeKeyboard() {
        return new ReplyKeyboardRemove(true);
    }

}
