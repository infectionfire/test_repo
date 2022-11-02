package com.example.test_repo.service;

import com.example.test_repo.model.User;
import com.example.test_repo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getAllUsers() {
        return this.userRepository.getAllUsers();
    }

    public User getUserByLogin(String login) {
        return this.userRepository.getUserByLogin(login);
    }

}
