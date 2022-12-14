package com.example.test_repo.repository;

import com.example.test_repo.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final List<User> users;

    public UserRepository() {
        this.users = List.of(
                new User("1", "1", "Andrej", "Golev", "a@aq.com", 32),
                new User("2", "2", "Pavel", "Sokolov", "1@qa.com", 23));
    }

    public User getUserByLogin(String login) {
        return users.stream()
                .filter(user -> login.equals(user.getLogin()))
                .findFirst()
                .orElse(null);
    }

    public List<User> getAllUsers() {
        return this.users;
    }


}
