package com.trainbooking.controller;

import com.trainbooking.dto.UserResponseDTO;
import com.trainbooking.entity.User;
import com.trainbooking.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody User user) {

        User savedUser = userService.register(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }
}