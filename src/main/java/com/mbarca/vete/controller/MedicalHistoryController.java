package com.mbarca.vete.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbarca.vete.dto.request.MedicalHistoryRequestDto;
import com.mbarca.vete.dto.response.MedicalHistoryResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.service.FileStorageService;
import com.mbarca.vete.service.MedicalHistoryService;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/medicalHistory")
public class MedicalHistoryController {
    private final MedicalHistoryService medicalHistoryService;
    private final FileStorageService fileStorageService;

    public MedicalHistoryController(MedicalHistoryService medicalHistoryService, FileStorageService fileStorageService) {
        this.medicalHistoryService = medicalHistoryService;
        this.fileStorageService = fileStorageService;
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<?> createMedicalHistoryController(@RequestParam(value = "medicalHistoryRequestDto") String medicalHistoryJson,
                                                            @RequestParam(value = "file", required = false) MultipartFile[] file) {
        try {
            MedicalHistoryRequestDto medicalHistoryRequestDto = new ObjectMapper().readValue(medicalHistoryJson, MedicalHistoryRequestDto.class);
            List<String> filePath = new ArrayList<>();
            if(file != null) {
                filePath = fileStorageService.store(file, "");
            }
            String response = medicalHistoryService.createMedicalHistory(medicalHistoryRequestDto, filePath.getFirst());
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

    @CrossOrigin
    @GetMapping("/downloadFile")
    public ResponseEntity<?> downloadFile(@RequestParam String filePath) {
        Resource file = fileStorageService.loadAsResource(filePath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
