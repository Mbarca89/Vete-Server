package com.mbarca.vete.service;

import com.mbarca.vete.dto.request.UserRequestDto;
import com.mbarca.vete.dto.response.UserResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.exceptions.UserNotFoundException;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {
    String createUser (UserRequestDto userRequestDto) throws MissingDataException, NoSuchAlgorithmException;
    String deleteUser (String name);
    List<UserResponseDto> getUsers();

    String editUser (UserRequestDto userRequestDto) throws MissingDataException, NoSuchAlgorithmException, UserNotFoundException;
}
