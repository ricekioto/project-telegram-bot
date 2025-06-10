package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.bot.Bot;
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
    private SilentSender sender;
    private SendMessage sendMessage;
    private EnglishRandomService englishRandomService;
    private ResponseHandler responseHandler;
    private List<Long> every10Seconds;
    private List<Long> every30Minutes;
    private List<Long> every60Minutes;

    public ScheduleService(EnglishRandomService englishRandomService, Bot bot) {
        this.englishRandomService = englishRandomService;
        this.responseHandler = bot.getResponseHandler();
        this.sendMessage = responseHandler.getSendMessage();
        this.sender = responseHandler.getSender();
        every10Seconds = responseHandler.getEvery10Seconds();
        every30Minutes = responseHandler.getEvery30Minutes();
        every60Minutes = responseHandler.getEvery60Minutes();
    }

    @Async
    @Scheduled(cron = "*/10 * * * * *")
    public void interlvalTime10Second() {
        try {
            for (Long instance : every10Seconds) {
                sendMessage.setChatId(instance);
                String messageText = englishRandomService.getSentence();
                sendMessage.setText(messageText);
                sender.execute(sendMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    @Scheduled(cron = "0 */30 * * * *")
    public void interlvalTime30Minute() {
        try {
            for (Long instance : every30Minutes) {
                sendMessage.setChatId(instance);
                String messageText = englishRandomService.getSentence();
                sendMessage.setText(messageText);
                sender.execute(sendMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    @Scheduled(cron = "0 0 * * * *")
    public void interlvalTime60Minute() {
        try {
            for (Long instance : every60Minutes) {
                sendMessage.setChatId(instance);
                String messageText = englishRandomService.getSentence();
                sendMessage.setText(messageText);
                sender.execute(sendMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
