package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.User;
import com.mbarca.vete.dto.request.UserRequestDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.repository.UserRepository;
import com.mbarca.vete.service.UserService;
import com.mbarca.vete.utils.PasswordHash;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String createUser(UserRequestDto userRequestDto) throws MissingDataException, NoSuchAlgorithmException {
        if (userRequestDto.getName() == null ||
                userRequestDto.getPassword() == null ||
                userRequestDto.getRole() == null || Objects.equals(userRequestDto.getName(), "")
                || Objects.equals(userRequestDto.getPassword(), "") ||
                Objects.equals(userRequestDto.getRole(), "")) {
            throw new MissingDataException("Faltan datos!");
        }

        User user = mapDtoToUser(userRequestDto);
        Integer response = userRepository.createUser(user);

        if (response.equals(0)){
            return "Error al crear el usuario!";
        }
        return "Usuario creado correctamente!";
    }

    @Override
    public String deleteUser(String name){
        Integer response = userRepository.deleteUser(name);
        if (response.equals(0)) {
            throw new EmptyResultDataAccessException(1);
        }
        return "Usuario eliminado correctamente";
    }

    private User mapDtoToUser(UserRequestDto userRequestDto) throws NoSuchAlgorithmException {

        String hashedPassword = PasswordHash.hashPassword(userRequestDto.getPassword());

        User user = new User();
        user.setName(userRequestDto.getName());
        user.setPassword(hashedPassword);
        user.setRole(userRequestDto.getRole());
        return user;
    }
}

