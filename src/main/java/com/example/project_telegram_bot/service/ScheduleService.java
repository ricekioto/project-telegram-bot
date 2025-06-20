package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.bot.Bot;
import com.example.project_telegram_bot.error.ScheduleServiceException;
import lombok.Getter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Getter
@Service
public class ScheduleService {
    private final String URL_RANDOM_SENTENCE_CONTROLLER;
    private SilentSender sender;
    private ResponseHandlerService responseHandlerService;
    private RequestService requestService;
    private List<Long> every10Seconds;
    private List<Long> every30Minutes;
    private List<Long> every60Minutes;

    public ScheduleService(EnglishRandomService englishRandomService, Bot bot) {
        this.responseHandlerService = bot.getResponseHandler();
        this.sender = responseHandlerService.getSender();
        this.requestService = responseHandlerService.getRequestService();
        this.URL_RANDOM_SENTENCE_CONTROLLER = responseHandlerService.getURL_RANDOM_SENTENCE_CONTROLLER();
        every10Seconds = responseHandlerService.getEvery10Seconds();
        every30Minutes = responseHandlerService.getEvery30Minutes();
        every60Minutes = responseHandlerService.getEvery60Minutes();
    }

    @Async
    @Scheduled(cron = "0 */5 * * * *")
    public void interlvalTime10Second() throws ScheduleServiceException {
        SendMessage sendMessage = new SendMessage();

        if (every10Seconds.isEmpty()) {
            return;
        }
        String messageText = requestService.get(URL_RANDOM_SENTENCE_CONTROLLER);
        for (Long instance : every10Seconds) {
            sendMessage.setChatId(instance);
            sendMessage.setText(messageText);
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
        String messageText = requestService.get(URL_RANDOM_SENTENCE_CONTROLLER);
        for (Long instance : every30Minutes) {
            sendMessage.setChatId(instance);
            sendMessage.setText(messageText);
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
        String messageText = requestService.get(URL_RANDOM_SENTENCE_CONTROLLER);
        for (Long instance : every60Minutes) {
            sendMessage.setChatId(instance);
            sendMessage.setText(messageText);
            sender.execute(sendMessage);
        }
    }
}
