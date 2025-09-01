package com.example.project_telegram_bot.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RequestStateService {

    private final Map<UUID, Long> requestIdToChatIdMap = new ConcurrentHashMap<>();

    private final Map<UUID, String> generatedSentencesMap = new ConcurrentHashMap<>();

    public void addRequestIdToChatIdMapping(UUID requestId, Long chatId) {
        requestIdToChatIdMap.put(requestId, chatId);
    }

    public Long getChatIdByRequestId(UUID requestId) {
        return requestIdToChatIdMap.get(requestId);
    }

    public void addGeneratedSentence(UUID requestId, String generatedSentence) {
        generatedSentencesMap.put(requestId, generatedSentence);
    }

    public String getGeneratedSentenceByRequestId(UUID requestId) {
        return generatedSentencesMap.get(requestId);
    }

    public void removeRequestIdMapping(UUID requestId) {
        requestIdToChatIdMap.remove(requestId);
        generatedSentencesMap.remove(requestId);
    }
}
