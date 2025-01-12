package com.example.financeadvisor.userservice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private int freeQuestions = 5; // по умолчанию даем 5 бесплатных вопросов

    public User() {}

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFreeQuestions() {
        return freeQuestions;
    }

    public void setFreeQuestions(int freeQuestions) {
        this.freeQuestions = freeQuestions;
    }

    // Метод для использования одного бесплатного вопроса
    public void useFreeQuestion() {
        if (this.freeQuestions > 0) {
            this.freeQuestions--;
        }
    }
}
