package com.example.financeadvisor.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Метод для создания пользователя
    public String createUser(String username, String email) {
        if (userRepository.findByUsername(username) != null) {
            return "Username is already taken.";
        }
        User user = new User(username, email);
        userRepository.save(user);
        return "User created successfully!";
    }

    // Метод для поиска пользователя по имени
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Метод для уменьшения количества бесплатных вопросов
    public void useFreeQuestion(String username) {
        User user = findUserByUsername(username);
        if (user != null) {
            user.useFreeQuestion();
            userRepository.save(user);
        }
    }

    // Метод для получения количества бесплатных вопросов
    public int getFreeQuestions(String username) {
        User user = findUserByUsername(username);
        return user != null ? user.getFreeQuestions() : 0;
    }

    // Метод для увеличения количества вопросов
    public void addQuestions(String username, int questions) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User with username " + username + " does not exist.");
        }
        user.setFreeQuestions(user.getFreeQuestions() + questions);
        userRepository.save(user);
    }
}
