package com.mbarca.vete.service;

import com.mbarca.vete.dto.request.LoginRequestDto;
import com.mbarca.vete.dto.response.AuthResponseDto;
import com.mbarca.vete.exceptions.UserNotFoundException;

public interface AuthService {
    public AuthResponseDto login (LoginRequestDto request) throws UserNotFoundException;
}
