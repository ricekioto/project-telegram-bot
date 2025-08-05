package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.error.RequestServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RestTemplate restTemplate;

    public String get(String url) throws RequestServiceException {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new RequestServiceException("Не удалось получить HTML. Код состояния: " + responseEntity.getStatusCode());
        }
    }
}








