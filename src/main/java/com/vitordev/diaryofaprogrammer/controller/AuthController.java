package com.vitordev.diaryofaprogrammer.controller;

import com.vitordev.diaryofaprogrammer.domain.user.User;
import com.vitordev.diaryofaprogrammer.domain.user.UserUtils;
import com.vitordev.diaryofaprogrammer.dto.AuthResponseDTO;
import com.vitordev.diaryofaprogrammer.dto.RequestLoginDTO;
import com.vitordev.diaryofaprogrammer.infra.security.TokenService;
import com.vitordev.diaryofaprogrammer.service.UserServices;
import com.vitordev.diaryofaprogrammer.service.exceptions.DataAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserServices userServices;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserUtils userUtils;

    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody User user) {

        userUtils.validateRegisterFields(user);

        if(userServices.findByEmail(user.getEmail()).isPresent()){
            throw new DataAlreadyExistsException("Email já cadastrado");
        };

        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setCreatedAt(new Date());
        user = userServices.save(user);

        String token = tokenService.generateToken(user);
        return ResponseEntity.ok().body(new AuthResponseDTO(token));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody RequestLoginDTO body) {

        userUtils.validateLoginFields(body);

        var usernamePassword = new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok().body(new AuthResponseDTO(token));
    }
}
