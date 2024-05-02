package com.mbarca.vete.controller;

import com.mbarca.vete.domain.MedicalHistory;
import com.mbarca.vete.dto.request.MedicalHistoryRequestDto;
import com.mbarca.vete.dto.response.MedicalHistoryResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.service.MedicalHistoryService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicalHistory")
public class MedicalHistoryController {
    private final MedicalHistoryService medicalHistoryService;

    public MedicalHistoryController(MedicalHistoryService medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<?> createMedicalHistoryController(@RequestBody MedicalHistoryRequestDto medicalHistoryRequestDto) {
        try {
            String response = medicalHistoryService.createMedicalHistory(medicalHistoryRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error" + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getMedicalHistoryForPet")
    public ResponseEntity<?> getMedicalHistoryForPetHandler (@RequestParam Long petId) {
        try {
            List<MedicalHistoryResponseDto> medicalHistory = medicalHistoryService.getMedicalHistoryForPet(petId);
            return ResponseEntity.status(HttpStatus.OK).body(medicalHistory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getMedicalHistoryById")
    public ResponseEntity<?> getMedicalHistoryByIdHandler (@RequestParam Long medicalHistoryId) {
        try {
            MedicalHistoryResponseDto medicalHistoryResponse = medicalHistoryService.getMedicalHistoryById(medicalHistoryId);
            return ResponseEntity.status(HttpStatus.OK).body(medicalHistoryResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMedicalHistoryHandler (@RequestParam Long medicalHistoryId) {
        try {
            String response = medicalHistoryService.deleteMedicalHistory(medicalHistoryId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Historia clínica no encontrada!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
