package com.example.financeadvisor.chatservice;

import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatService {

    private static final String OPENAI_API_KEY = "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";
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String getChatGptResponse(String question) {
        // Создание RestTemplate для отправки запросов
        RestTemplate restTemplate = new RestTemplate();

        // Настройка заголовков
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Создание тела запроса с вопросом
        String body = "{"
                + "\"model\": \"gpt-3.5-turbo\","
                + "\"messages\": [{\"role\": \"user\", \"content\": \"" + question + "\"}],"
                + "\"max_tokens\": 150"
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // Отправка POST-запроса и получение ответа
        ResponseEntity<String> response = restTemplate.exchange(OPENAI_URL, HttpMethod.POST, entity, String.class);

        // Извлечение ответа из JSON (упрощенный вариант)
        String responseBody = response.getBody();

        // Простой способ извлечь ответ (в реальном приложении лучше использовать парсинг JSON через библиотеку)
        String answer = responseBody.split("\"content\":\"")[1].split("\"}")[0];

        return answer;
    }
}
