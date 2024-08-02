package com.mbarca.vete.controller;

import com.mbarca.vete.dto.request.VaccineRequestDto;
import com.mbarca.vete.dto.response.ReminderResponseDto;
import com.mbarca.vete.dto.response.VaccineResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.service.VaccineService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vaccines")
public class VaccineController {

    VaccineService vaccineService;

    public VaccineController(VaccineService vaccineService) {
        this.vaccineService = vaccineService;
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<String> createVaccineHandler(@RequestBody VaccineRequestDto vaccineRequestDto) {
        try {
            String response = vaccineService.createVaccine(vaccineRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVaccineHandler(@PathVariable Long id){
        try{
            String response = vaccineService.deleteVaccine(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Evento no encontrado!");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getVaccines/{petId}")
    public ResponseEntity<?> getVaccinesHandler (@PathVariable Long petId) {
        try {
            List<VaccineResponseDto> response = vaccineService.getVaccinesById(petId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getVaccinesByDate")
    public ResponseEntity<?> getVaccinesByDateHandler (@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        try {
            List<ReminderResponseDto> response = vaccineService.getVaccinesByDate(date);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @CrossOrigin
    @PutMapping("/editVaccine")
    public ResponseEntity<?> editVaccineHandler (@RequestBody VaccineRequestDto vaccineRequestDto) {
        try {
            String response = vaccineService.editVaccine(vaccineRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
