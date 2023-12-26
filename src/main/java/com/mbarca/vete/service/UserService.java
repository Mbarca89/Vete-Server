package com.mbarca.vete.service;

import com.mbarca.vete.dto.request.UserRequestDto;
import com.mbarca.vete.exceptions.MissingDataException;

import java.security.NoSuchAlgorithmException;

public interface UserService {
    String createUser (UserRequestDto userRequestDto) throws MissingDataException, NoSuchAlgorithmException;

}
