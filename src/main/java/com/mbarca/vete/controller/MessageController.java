package com.mbarca.vete.controller;

import com.mbarca.vete.dto.response.MessageResponseDto;
import com.mbarca.vete.service.MessageService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @CrossOrigin
    @PostMapping("/force")
    public ResponseEntity<String> forceMessagesHandler() {
        try {
            String response = messageService.forceMessage();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/forceReminders")
    public ResponseEntity<String> forceRemindersHandler() {
        try {
            String response = messageService.forceReminders();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getMessages")
    public ResponseEntity<?> getMessagesHandler (@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        try {
            List<MessageResponseDto> response = messageService.getMessages(date);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
