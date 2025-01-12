package com.example.financeadvisor.paymentservice;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

@Service
public class PaymentService {

    private static final String SHOP_ID = "1011519";
    private static final String SECRET_KEY = "test__3jODgs14TUCY4ofS2oW9UyuZ2R6Rptc8rdB5xViblg";

    // Метод для создания запроса на оплату
    public String createPaymentRequest(double amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(SHOP_ID, SECRET_KEY);
        headers.set("Idempotence-Key", UUID.randomUUID().toString());
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        String body = "{"
                + "\"amount\": {"
                + "\"value\": \"" + amount + "\","
                + "\"currency\": \"RUB\""
                + "},"
                + "\"capture\": true,"
                + "\"confirmation\": {"
                + "\"type\": \"redirect\","
                + "\"return_url\": \"http://localhost:8080/success\""
                + "},"
                + "\"description\": \"Консультация по финансовым вопросам\""
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.yookassa.ru/v3/payments",
                HttpMethod.POST,
                entity,
                String.class);

        return response.getBody();
    }

    // Метод для проверки статуса платежа
    public String checkPaymentStatus(String paymentId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(SHOP_ID, SECRET_KEY);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.yookassa.ru/v3/payments/" + paymentId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        return response.getBody();
    }
}
