package com.example.test_repo.rest;

import com.example.test_repo.model.User;
import com.example.test_repo.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody User getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        User user = principal instanceof User ? (User) principal : null;
        return Objects.nonNull(user) ? this.userService.getUserByLogin(user.getLogin()) : null;
    }
}
