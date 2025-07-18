package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.bot.Bot;
import com.example.project_telegram_bot.error.ScheduleServiceException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

import static com.example.project_telegram_bot.service.MarkdownV2Service.escapeMarkdownV2;

@Getter
@Service
public class ScheduleService {
    @Value("${url.random-controller}")
    private String generatorControllerUrl;
    private SilentSender sender;
    private ResponseHandlerService responseHandlerService;
    private RequestService requestService;
    private BuildingUrlService buildingUrlService;
    private List<Long> every10Seconds;
    private List<Long> every30Minutes;
    private List<Long> every60Minutes;

    public ScheduleService(Bot bot, RequestService requestService, BuildingUrlService buildingUrlService) {
        this.responseHandlerService = bot.getResponseHandlerService();
        this.sender = responseHandlerService.getSender();
        this.requestService = requestService;
        this.buildingUrlService = buildingUrlService;
        every10Seconds = responseHandlerService.getEvery10Seconds();
        every30Minutes = responseHandlerService.getEvery30Minutes();
        every60Minutes = responseHandlerService.getEvery60Minutes();
    }

    @Async
    @Scheduled(cron = "0 */15 * * * *")
    public void interlvalTime10Second() throws ScheduleServiceException {
        SendMessage sendMessage = new SendMessage();

        if (every10Seconds.isEmpty()) {
            return;
        }
        String messageText = requestService.get(generatorControllerUrl);
        String translationControllerUrl = buildingUrlService.getTranslationControllerUrl(messageText);
        String translatedText = requestService.get(translationControllerUrl);

        translatedText = escapeMarkdownV2(translatedText);
        messageText = escapeMarkdownV2(messageText);
        String returnText = messageText + "\n\n||" + translatedText + "||";
        for (Long instance : every10Seconds) {
            sendMessage.setChatId(instance);
            sendMessage.setText(returnText);
            sendMessage.setParseMode("MARKDOWNV2");
            sender.execute(sendMessage);
        }
    }

    @Async
    @Scheduled(cron = "0 */30 * * * *")
    public void interlvalTime30Minute() throws ScheduleServiceException {
        SendMessage sendMessage = new SendMessage();

        if (every30Minutes.isEmpty()) {
            return;
        }
        String messageText = requestService.get(generatorControllerUrl);
        String translationControllerUrl = buildingUrlService.getTranslationControllerUrl(messageText);
        String translatedText = requestService.get(translationControllerUrl);

        translatedText = escapeMarkdownV2(translatedText);
        messageText = escapeMarkdownV2(messageText);
        String returnText = messageText + "\n\n||" + translatedText + "||";
        for (Long instance : every30Minutes) {
            sendMessage.setChatId(instance);
            sendMessage.setText(returnText);
            sendMessage.setParseMode("MARKDOWNV2");
            sender.execute(sendMessage);
        }
    }

    @Async
    @Scheduled(cron = "0 0 * * * *")
    public void interlvalTime60Minute() throws ScheduleServiceException {
        SendMessage sendMessage = new SendMessage();

        if (every60Minutes.isEmpty()) {
            return;
        }
        String messageText = requestService.get(generatorControllerUrl);
        String translationControllerUrl = buildingUrlService.getTranslationControllerUrl(messageText);
        String translatedText = requestService.get(translationControllerUrl);

        translatedText = escapeMarkdownV2(translatedText);
        messageText = escapeMarkdownV2(messageText);
        String returnText = messageText + "\n\n||" + translatedText + "||";
        for (Long instance : every60Minutes) {
            sendMessage.setChatId(instance);
            sendMessage.setText(returnText);
            sendMessage.setParseMode("MARKDOWNV2");
            sender.execute(sendMessage);
        }
    }
}



