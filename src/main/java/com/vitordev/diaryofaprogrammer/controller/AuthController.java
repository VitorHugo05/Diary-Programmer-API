package com.vitordev.diaryofaprogrammer.controller;

import com.vitordev.diaryofaprogrammer.domain.User;
import com.vitordev.diaryofaprogrammer.dto.ResponseLoginDTO;
import com.vitordev.diaryofaprogrammer.dto.ResponseRegisterDTO;
import com.vitordev.diaryofaprogrammer.service.TokenService;
import com.vitordev.diaryofaprogrammer.service.UserServices;
import com.vitordev.diaryofaprogrammer.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private TokenService tokenService;

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseRegisterDTO> register(@RequestBody User user) {
        user.setCreatedAt(new Date());
        user = userServices.insert(user);
        String token = tokenService.generateToken(user);
        return ResponseEntity.ok().body(new ResponseRegisterDTO(user.getId(), token));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseRegisterDTO> login(@RequestBody ResponseLoginDTO body) {
        User user = userServices.findByEmail(body.getEmail()).orElseThrow(() -> new ObjectNotFoundException("Usuario não encontrado"));
        if(body.getPassword().equals(user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseRegisterDTO(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

}
