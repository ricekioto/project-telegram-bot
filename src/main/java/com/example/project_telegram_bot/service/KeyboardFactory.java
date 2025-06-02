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
        return new ReplyKeyboardMarkup(List.of());
    }

    public static ReplyKeyboard getPizzaToppingsKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add("так");
        row.add("или вот так");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public static ReplyKeyboard getPizzaOrDrinkKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add("окака");
        row.add("зачем");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public static ReplyKeyboard getYesOrNo() {
        KeyboardRow row = new KeyboardRow();
        row.add("конец");
        row.add("снова");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public static ReplyKeyboard closeKeyboard() {
        return new ReplyKeyboardRemove(true);
    }

}
