package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.User;
import com.mbarca.vete.dto.request.UserRequestDto;
import com.mbarca.vete.dto.response.UserResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.exceptions.UserNotFoundException;
import com.mbarca.vete.repository.UserRepository;
import com.mbarca.vete.service.UserService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String createUser(UserRequestDto userRequestDto) throws MissingDataException, NoSuchAlgorithmException {
        if (userRequestDto.getUserName() == null ||
                userRequestDto.getPassword() == null ||
                userRequestDto.getRole() == null || Objects.equals(userRequestDto.getUserName(), "")
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

    @Override
    public List<UserResponseDto> getUsers() {
        List<User> users = userRepository.getUsers();
        return users.stream().map(this::mapUserToDto).collect(Collectors.toList());
    }

    public String editUser (UserRequestDto userRequestDto) throws MissingDataException, NoSuchAlgorithmException, UserNotFoundException {
        if (userRequestDto.getUserName() == null ||
                userRequestDto.getRole() == null || Objects.equals(userRequestDto.getUserName(), "")
                || Objects.equals(userRequestDto.getRole(), "")) {
            throw new MissingDataException("Faltan datos!");
        }

        User user = mapDtoToUser(userRequestDto);
        Integer response = userRepository.editUser(user);

        if (response.equals(0)){
            return "Error al editar el usuario!";
        }
        return "Usuario editado correctamente!";
    }

    private User mapDtoToUser(UserRequestDto userRequestDto) throws NoSuchAlgorithmException {

        String hashedPassword = passwordEncoder.encode(userRequestDto.getPassword());

        User user = new User();
        if(userRequestDto.getId() != null) user.setId(Long.valueOf(userRequestDto.getId()));
        user.setName(userRequestDto.getName());
        user.setSurname(userRequestDto.getSurname());
        user.setUserName(userRequestDto.getUserName());
        user.setPassword(hashedPassword);
        user.setRole(userRequestDto.getRole());
        return user;
    }

    private UserResponseDto mapUserToDto (User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setSurname(user.getSurname());
        userResponseDto.setRole(user.getRole());
        userResponseDto.setUserName(user.getUserName());
        return userResponseDto;
    }
}

