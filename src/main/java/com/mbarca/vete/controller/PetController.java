package com.mbarca.vete.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbarca.vete.domain.Images;
import com.mbarca.vete.domain.PaginatedResults;
import com.mbarca.vete.dto.request.PetRequestDto;
import com.mbarca.vete.dto.response.PetResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.service.PetService;
import com.mbarca.vete.utils.ImageCompressor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pets")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<String> createPetHandler(@RequestParam(value = "file", required = false) MultipartFile file,
                                                   @RequestParam("pet") String petJson,
                                                   @RequestParam("clientId") String clientId) {
        try {
            PetRequestDto petRequestDto = new ObjectMapper().readValue(petJson, PetRequestDto.class);
            Images images = new Images();
            if (file != null && !file.isEmpty()) {
                images = ImageCompressor.compressImage(file.getBytes());
            }
            String response = petService.createPet(petRequestDto, images, Long.parseLong(clientId));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error json" + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error de IO" + e.getMessage());
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (MaxUploadSizeExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La imagen es demasiado grande");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error" + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getPets")
    public ResponseEntity<?> getAllPetsHandler(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "12") int size) {
        try {
            PaginatedResults<PetResponseDto> pets = petService.getAllPets(page, size);
            return ResponseEntity.status(HttpStatus.OK).body(pets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getPetClient")
    public ResponseEntity<?> getPetsFromClientHandler(@RequestParam Long clientId) {
        try {
            List<PetResponseDto> pets = petService.getPetsFromClient(clientId);
            return ResponseEntity.status(HttpStatus.OK).body(pets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getPetsByName")
    public ResponseEntity<?> getPetsByNameHandler(@RequestParam(defaultValue = "") String name,
                                                  @RequestParam(defaultValue = "") String species,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "12") int size) {
        try {
            PaginatedResults<PetResponseDto> pets = petService.getPetsByName(name, species, page, size);
            return ResponseEntity.status(HttpStatus.OK).body(pets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("getPetById")
    public ResponseEntity<?> getPetById(@RequestParam Long petId) {
        try {
            PetResponseDto pet = petService.getPetById(petId);
            return ResponseEntity.status(HttpStatus.OK).body(pet);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePetHandler(@RequestParam Long petId) {
        try {
            String response = petService.deletePet(petId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mascota no encontrada!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getPetCount")
    public ResponseEntity<?> getPetCountHandler() {
        try {
            int count = petService.getPetCount();
            return ResponseEntity.status(HttpStatus.OK).body(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/edit")
    public ResponseEntity<String> editUserHandler(@RequestParam(value = "file", required = false) MultipartFile file,
                                                  @RequestParam("pet") String petJson) {
        try {
            PetRequestDto petRequestDto = new ObjectMapper().readValue(petJson, PetRequestDto.class);
            Images images = new Images();
            if (file != null && !file.isEmpty()) {
                images = ImageCompressor.compressImage(file.getBytes());
            }
            String response = petService.editPet(petRequestDto, images);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No existe una mascota con ese nombre!");
        } catch (MaxUploadSizeExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La imagen es demasiado grande");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Imagen no soportada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception: " + e.getMessage());
        }
    }
}
