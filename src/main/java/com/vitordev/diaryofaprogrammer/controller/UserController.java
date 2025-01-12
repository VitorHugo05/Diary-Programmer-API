package com.vitordev.diaryofaprogrammer.controller;

import com.vitordev.diaryofaprogrammer.domain.User;
import com.vitordev.diaryofaprogrammer.dto.UserDTO;
import com.vitordev.diaryofaprogrammer.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserServices userServices;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findUsers() {
        List<User> list = userServices.findAll();
        List<UserDTO> listDto = list.stream().map(UserDTO::new).toList();
        return ResponseEntity.ok().body(listDto);
    }

    @PostMapping
    public ResponseEntity<Void> insertUser(@RequestBody User user) {
        userServices.insert(user);
        return ResponseEntity.noContent().build();
    }
}
