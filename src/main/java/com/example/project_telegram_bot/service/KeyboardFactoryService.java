package com.example.project_telegram_bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardFactoryService {
    private final ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();

    public ReplyKeyboard toStart() {
        List<KeyboardRow> keyboardStart = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("/start");
        keyboardStart.add(row);

        markup.setKeyboard(keyboardStart);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);
        return markup;
    }

    public ReplyKeyboard getSentenceAndStop() {
        List<KeyboardRow> keyboardMenu = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Получить");
        row1.add("15 минут");
        keyboardMenu.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add("30 минут");
        row2.add("60 минут");
        keyboardMenu.add(row2);

        KeyboardRow row3 = new KeyboardRow();
        row3.add("Не отправлять по расписанию");
        row3.add("Остановить бота");
        keyboardMenu.add(row3);

        markup.setKeyboard(keyboardMenu);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);

        return markup;
    }

    public ReplyKeyboard closeKeyboard() {
        return new ReplyKeyboardRemove(true);
    }
}

