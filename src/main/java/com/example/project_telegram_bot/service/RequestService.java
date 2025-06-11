package com.example.project_telegram_bot.service;

import com.example.project_telegram_bot.error.RequestServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RequestService {

    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    private final RestTemplate restTemplate;

    public RequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getHtml(String url) {
        logger.info("Запрос HTML по URL: {}", url);
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                logger.info("Успешно получен HTML по URL: {}", url);
                return responseEntity.getBody();
            } else {
                logger.warn("Не удалось получить HTML по URL: {}. Код состояния: {}", url, responseEntity.getStatusCode());
                throw new RequestServiceException("Не удалось получить HTML. Код состояния: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Ошибка при запросе HTML с URL: {}. Ошибка: {}", url, e.getMessage(), e);
            throw new RequestServiceException("Ошибка при запросе HTML: " + e.getMessage(), e);
        }
    }
}
