package com.mbarca.vete.controller;

import com.mbarca.vete.dto.response.OrderResponseDto;
import com.mbarca.vete.dto.response.PublicPetProfileResponseDto;
import com.mbarca.vete.service.PublicPetProfileService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/pet")
public class PublicPetController {
    private final PublicPetProfileService publicPetProfileService;

    public PublicPetController(PublicPetProfileService publicPetProfileService) {
        this.publicPetProfileService = publicPetProfileService;
    }

    @CrossOrigin
    @GetMapping("")
    public ResponseEntity<?> getOrdersByDateHandler(@RequestParam("publicId") UUID publicPetId) {
        try {
            PublicPetProfileResponseDto response = publicPetProfileService.getPetProfile(publicPetId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
