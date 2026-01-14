package com.mbarca.vete.controller;

import com.mbarca.vete.dto.response.SaleResponseDto;
import com.mbarca.vete.dto.response.WebOrderResponseDto;
import com.mbarca.vete.dto.response.WebOrderResponseWithItemsDto;
import com.mbarca.vete.service.WebOrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/webOrder")
public class WebOrderController {
    private final WebOrderService webOrderService;

    public WebOrderController(WebOrderService webOrderService) {
        this.webOrderService = webOrderService;
    }

    @CrossOrigin
    @GetMapping("/getOrdersByDate")
    public ResponseEntity<?> getOrdersByDateHandler(
            @RequestParam("dateStart") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
            @RequestParam("dateEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd) {
        try {
            List<WebOrderResponseDto> response = webOrderService.getOrdersByDate(dateStart, dateEnd);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getOrderById")
    public ResponseEntity<?> getOrderByIdHandler(
            @RequestParam Long orderId) {
        try {
            WebOrderResponseWithItemsDto response = webOrderService.getOrderById(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @PutMapping("/shipOrder")
    public ResponseEntity<?> shipOrderHandler(@RequestParam Long orderId) {
        try {
            String response = webOrderService.shipOrder(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
