package com.mbarca.vete.controller;

import com.mbarca.vete.dto.request.UserRequestDto;
import com.mbarca.vete.dto.response.UserResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.service.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<String> createUserHandler(@RequestBody UserRequestDto userRequestDto) {
        try {
            String response = userService.createUser(userRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ya existe un usuario con ese nombre!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deleteUserHandler(@PathVariable String name){
        try{
            String response = userService.deleteUser(name);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario no encontrado!");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsersHandler () {
        try {
            List<UserResponseDto> response = userService.getUsers();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/edit")
    public ResponseEntity<String> editUserHandler(@RequestBody UserRequestDto userRequestDto) {
        try {
            String response = userService.editUser(userRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No existe un usuario con ese nombre!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
