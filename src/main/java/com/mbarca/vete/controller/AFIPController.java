package com.mbarca.vete.controller;

import com.mbarca.vete.domain.AfipResponse;
import com.mbarca.vete.domain.WSAAAuthResponse;
import com.mbarca.vete.dto.request.BillRequestDto;
import com.mbarca.vete.dto.request.CategoryRequestDto;
import com.mbarca.vete.service.AfipService;
import com.mbarca.vete.service.WSAAService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/afipws")
public class AFIPController {
    AfipService afipService;

    public AFIPController(AfipService afipService) {
        this.afipService = afipService;
    }

    @CrossOrigin
    @GetMapping("/obtenerPtosVenta")
    public ResponseEntity<?> obtenerPtosVentaHandler() {
        try {
            String response = afipService.consultarPuntosVenta();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/obtenerUltimoComprobante")
    public ResponseEntity<?> obtenerUltimoComprobanteHandler(@RequestParam String type) {
        try {
            String response = afipService.consultarUltimoComprobante(type);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/generarComprobante")
    public ResponseEntity<?> generarComprobanteHandler(@RequestBody BillRequestDto billRequestDto) {
        try {
            AfipResponse response = afipService.generarComprobante(billRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
