package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.entity.SentenceResponse;
import com.example.project_telegram_bot.error.RequestServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RequestService {
    private final RestTemplate restTemplate;

    public RequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getHtml(String url) throws RequestServiceException {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new RequestServiceException("Не удалось получить HTML. Код состояния: " + responseEntity.getStatusCode());
        }
    }

    public String get(String url) throws RequestServiceException {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new RequestServiceException("Не удалось получить HTML. Код состояния: " + responseEntity.getStatusCode());
        }
    }

    public SentenceResponse getSentenceResponse(String url) throws RequestServiceException {
        SentenceResponse[] responseEntity = restTemplate.getForObject(url, SentenceResponse[].class);
        if (responseEntity.length != 0) {
            return responseEntity[0];
        } else {
            return null;
        }

    }
}
