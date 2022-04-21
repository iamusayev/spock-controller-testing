package com.example.spockrepeateverything.controller;

import com.example.spockrepeateverything.model.dto.UserRequestDto;
import com.example.spockrepeateverything.model.dto.UserResponseDto;
import com.example.spockrepeateverything.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserRequestDto dto) {
        userService.createUser(dto);
    }


    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserResponseDto> getAll() {
        return userService.getAllUsers();
    }


}
