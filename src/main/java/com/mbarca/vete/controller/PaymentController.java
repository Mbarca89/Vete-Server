package com.mbarca.vete.controller;

import com.mbarca.vete.dto.request.PaymentRequestDto;
import com.mbarca.vete.dto.request.SaleRequestDto;
import com.mbarca.vete.dto.response.PaymentResponseDto;
import com.mbarca.vete.dto.response.SaleResponseDto;
import com.mbarca.vete.service.PaymentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<?> createPaymentHandler (@RequestBody PaymentRequestDto paymentRequestDto) {
        try {
            String response = paymentService.createPayment(paymentRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @CrossOrigin
    @PostMapping("/makePayment")
    public ResponseEntity<?> makePaymentHandler (@RequestBody PaymentRequestDto paymentRequestDto) {
        try {
            String response = paymentService.makePayment(paymentRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getById/{paymentId}")
    public ResponseEntity<?> getPaymentHandler (@PathVariable Long paymentId) {
        try {
            PaymentResponseDto response = paymentService.getPaymentById(paymentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getByDate")
    public ResponseEntity<?> getPaymentsByDateHandler (
            @RequestParam("dateStart") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
            @RequestParam("dateEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd){
        try {
            List<PaymentResponseDto> response = paymentService.getPayments(dateStart, dateEnd);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
