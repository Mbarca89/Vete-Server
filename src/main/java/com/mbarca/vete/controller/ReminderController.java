package com.mbarca.vete.controller;

import com.mbarca.vete.dto.request.ReminderRequestDto;
import com.mbarca.vete.dto.request.VaccineRequestDto;
import com.mbarca.vete.dto.response.ReminderResponseDto;
import com.mbarca.vete.dto.response.VaccineResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.service.ReminderService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reminders")
public class ReminderController {
    ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<String> createReminderHandler(@RequestBody ReminderRequestDto reminderRequestDto) {
        try {
            String response = reminderService.createReminder(reminderRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping("/delete/{reminderId}")
    public ResponseEntity<String> deleteReminderHandler(@PathVariable Long reminderId){
        try{
            String response = reminderService.deleteReminder(reminderId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Evento no encontrado!");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getReminders/{date}")
    public ResponseEntity<?> getVaccinesHandler (@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        try {
            List<ReminderResponseDto> response = reminderService.getReminders(date);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getReminder/{reminderId}")
    public ResponseEntity<?> getReminderHandler (@PathVariable Long reminderId) {
        try {
            ReminderResponseDto response = reminderService.getReminderById(reminderId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
