package com.mbarca.vete.controller;

import com.mbarca.vete.dto.request.UserRequestDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping("/api/v1/users")
public class UserController {
        private final UserService userService;

        public UserController(UserService userService) {
            this.userService = userService;
        }

        @PostMapping("/create")
        public ResponseEntity<String> createUserHandler(@RequestBody UserRequestDto userRequestDto) {
            try {
                String response = userService.createUser(userRequestDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }catch (MissingDataException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

            }
        }
    }
