package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.User;
import com.mbarca.vete.dto.request.LoginRequestDto;
import com.mbarca.vete.dto.response.AuthResponseDto;
import com.mbarca.vete.exceptions.UserNotFoundException;
import com.mbarca.vete.repository.UserRepository;
import com.mbarca.vete.service.AuthService;
import com.mbarca.vete.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) throws UserNotFoundException {
        String normalizedUserName = request.getUserName().toLowerCase();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(normalizedUserName, request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("Usuario o contraseña inválidos");
        } catch (AuthenticationException e) {
            throw new UserNotFoundException("No se pudo autenticar el usuario");
        }
        User user = userRepository.findUserByName(normalizedUserName);

        if (user == null) {
            throw new UserNotFoundException("Usuario no encontrado!");
        }
        String token = jwtService.getToken(user);
        AuthResponseDto response = new AuthResponseDto();
        response.setUserName(user.getUsername());
        response.setToken(token);
        response.setRole(user.getAuthorities());
        response.setName(user.getName());
        response.setSurname(user.getSurname());
        response.setId(user.getId());
        return response;
    }
}
