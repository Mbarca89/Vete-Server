package com.mbarca.vete.controller;

import com.mbarca.vete.dto.request.ProviderRequestDto;
import com.mbarca.vete.dto.request.UserRequestDto;
import com.mbarca.vete.dto.response.ProviderResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.service.ProviderService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/providers")
public class ProviderController {
    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<?> createProviderHandler(@RequestBody ProviderRequestDto providerRequestDto) {
        try {
            String response = providerService.createProvider(providerRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getProviders")
    public ResponseEntity<?> getAllProvidersHandler() {
        try {
            List<ProviderResponseDto> providers = providerService.getAllProviders();
            return ResponseEntity.status(HttpStatus.OK).body(providers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getProvidersNames")
    public ResponseEntity<?> getAllProvidersNamesHandler() {
        try {
            List<String> providers = providerService.getProvidersNames();
            return ResponseEntity.status(HttpStatus.OK).body(providers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("getByName/{name}")
    public ResponseEntity<?> getProviderByNameHandler(@PathVariable String name) {
        try {
            ProviderResponseDto response = providerService.getProviderByName(name);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Proveedor no encontrado!");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/edit")
    public ResponseEntity<String> editProviderHandler(@RequestBody ProviderRequestDto providerRequestDto) {
        try {
            String response = providerService.editProvider(providerRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No existe un proveedor con ese nombre!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProviderHandler(@RequestParam Long providerId){
        try{
            String response = providerService.deleteProvider(providerId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Proveedor no encontrado!");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El proveedor tiene productos asociados!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
