package com.example.financeadvisor.userservice;

import com.example.financeadvisor.chatservice.ChatService;
import com.example.financeadvisor.paymentservice.PaymentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private PaymentService paymentService;

    // Метод для создания пользователя
    @PostMapping
    public String createUser(@RequestParam String username, @RequestParam String email) {
        return userService.createUser(username, email);
    }

    // Метод для получения количества бесплатных вопросов
    @GetMapping("/free-questions/{username}")
    public int getFreeQuestions(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User with username " + username + " does not exist.");
        }
        return user.getFreeQuestions();
    }

    // Метод для использования одного бесплатного вопроса
    @PostMapping("/use-free-question")
    public String useFreeQuestion(@RequestParam String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return "Error: User with username " + username + " does not exist.";
        }
        userService.useFreeQuestion(username);
        return "Free question used successfully.";
    }

    // Метод для получения ответа от ChatGPT
    @GetMapping("/consult")
    public String getAnswer(@RequestParam String username, @RequestParam String question) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return "Error: User with username " + username + " does not exist.";
        }

        if (user.getFreeQuestions() > 0) {
            userService.useFreeQuestion(username);
            return chatService.getChatGptResponse(question);
        } else {
            return "You have used all your free questions. Please make a payment to continue.";
        }
    }

    // Метод для запроса на оплату
    @PostMapping("/user-pay")
    public String requestPayment(@RequestParam String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return "Error: User with username " + username + " does not exist.";
        }
        double amount = 50.00; // Цена за 10 вопросов
        return paymentService.createPaymentRequest(amount);
    }

    // Метод для обработки успешной оплаты
    @PostMapping("/payment-success")
    public String handlePaymentSuccess(@RequestParam String paymentId, @RequestParam String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return "Error: User with username " + username + " does not exist.";
        }

        String paymentResponse = paymentService.checkPaymentStatus(paymentId);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(paymentResponse);
            String status = rootNode.path("status").asText();

            if ("succeeded".equals(status)) {
                userService.addQuestions(username, 10); // Увеличиваем на 10 вопросов
                return "Payment successful! You now have 10 more questions.";
            } else {
                return "Payment not confirmed. Please try again.";
            }
        } catch (Exception e) {
            return "Error processing payment response: " + e.getMessage();
        }
    }
}
