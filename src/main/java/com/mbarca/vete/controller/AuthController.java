package com.mbarca.vete.controller;

import com.mbarca.vete.dto.request.LoginRequestDto;
import com.mbarca.vete.dto.response.AuthResponseDto;
import com.mbarca.vete.exceptions.UserNotFoundException;
import com.mbarca.vete.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        AuthResponseDto response = new AuthResponseDto();
        try {
            response = authService.login(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
