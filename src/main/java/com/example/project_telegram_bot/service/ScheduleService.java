package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.entity.Constants;
import com.example.project_telegram_bot.entity.UserTg;
import com.example.project_telegram_bot.entity.enums.Interval;
import com.example.project_telegram_bot.error.ScheduleServiceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

import static com.example.project_telegram_bot.entity.Constants.MARKDOWNV2;
import static com.example.project_telegram_bot.service.MarkdownV2Service.escapeMarkdownV2;

@Getter
@Service
@RequiredArgsConstructor
public class ScheduleService {
    @Value("${url.random-controller}")
    private String generatorControllerUrl;
    private final SilentSender sender;
    private final RequestService requestService;
    private final BuildingUrlService buildingUrlService;
    private final UserTgService userTgService;
    private final BotService botService;

    @Async
    @Scheduled(cron = "0 */15 * * * *")
    public void interlvalTime15Minute() throws ScheduleServiceException {
        SendMessage sendMessage = new SendMessage();
        List<UserTg> listUserTg = userTgService.findAllByInterval(Interval.INTERVAL15);
        if (listUserTg.isEmpty()) {
            return;
        }

        String messageText = requestService.get(generatorControllerUrl);
        String translationControllerUrl = buildingUrlService.getTranslationControllerUrl(messageText);
        String translatedText = requestService.get(translationControllerUrl);

        translatedText = escapeMarkdownV2(translatedText);
        messageText = escapeMarkdownV2(messageText);
        String returnText = messageText + "\n\n||" + translatedText + "||";
        for (UserTg instance : listUserTg) {
            sendMessage.setChatId(instance.getChatId());
            sendMessage.setText(returnText);
            sendMessage.setParseMode(Constants.MARKDOWNV2);
            sender.execute(sendMessage);
        }
    }

    @Async
    @Scheduled(cron = "0 */30 * * * *")
    public void interlvalTime30Minute() throws ScheduleServiceException {
        SendMessage sendMessage = new SendMessage();
        List<UserTg> listUserTg = userTgService.findAllByInterval(Interval.INTERVAL30);
        if (listUserTg.isEmpty()) {
            return;
        }
        String messageText = requestService.get(generatorControllerUrl);
        String translationControllerUrl = buildingUrlService.getTranslationControllerUrl(messageText);
        String translatedText = requestService.get(translationControllerUrl);

        translatedText = escapeMarkdownV2(translatedText);
        messageText = escapeMarkdownV2(messageText);
        String returnText = messageText + "\n\n||" + translatedText + "||";
        for (UserTg instance : listUserTg) {
            sendMessage.setChatId(instance.getChatId());
            sendMessage.setText(returnText);
            sendMessage.setParseMode(Constants.MARKDOWNV2);
            sender.execute(sendMessage);
        }
    }

    @Async
    @Scheduled(cron = "0 0 * * * *")
    public void interlvalTime60Minute() throws ScheduleServiceException {
        SendMessage sendMessage = new SendMessage();
        List<UserTg> listUserTg = userTgService.findAllByInterval(Interval.INTERVAL60);
        if (listUserTg.isEmpty()) {
            return;
        }
        String messageText = requestService.get(generatorControllerUrl);
        String translationControllerUrl = buildingUrlService.getTranslationControllerUrl(messageText);
        String translatedText = requestService.get(translationControllerUrl);

        translatedText = escapeMarkdownV2(translatedText);
        messageText = escapeMarkdownV2(messageText);
        String returnText = messageText + "\n\n||" + translatedText + "||";
        for (UserTg instance : listUserTg) {
            sendMessage.setChatId(instance.getChatId());
            sendMessage.setText(returnText);
            sendMessage.setParseMode(Constants.MARKDOWNV2);
            sender.execute(sendMessage);
        }
    }
}