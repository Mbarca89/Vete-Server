package com.mbarca.vete.service.impl;

import com.mbarca.vete.dto.request.LoginRequestDto;
import com.mbarca.vete.dto.response.AuthResponseDto;
import com.mbarca.vete.exceptions.UserNotFoundException;
import com.mbarca.vete.repository.AuthRepository;
import com.mbarca.vete.service.AuthService;
import com.mbarca.vete.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final AuthRepository authRepository;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, AuthRepository authRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.authRepository = authRepository;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) throws UserNotFoundException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        UserDetails user = authRepository.findUserByName(request.getUserName());
        if (user == null) {
            throw new UserNotFoundException("Usuario no encontrado!");
        }
        String token = jwtService.getToken(user);
        AuthResponseDto response = new AuthResponseDto();
        response.setToken(token);
        return response;
    }
}
