package com.example.financeadvisor.chatservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Эндпоинт для получения ответа от ChatGPT
    @PostMapping("/consult")
    public String getAnswer(@RequestBody QuestionRequest request) {
        // Здесь можно добавить логику для проверки пользователя, если необходимо
        return chatService.getChatGptResponse(request.getQuestion());
    }

    // Вспомогательный класс для запроса
    public static class QuestionRequest {
        private String username;
        private String question;

        // Геттеры и сеттеры
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }
    }
}
