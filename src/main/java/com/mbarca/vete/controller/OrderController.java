package com.mbarca.vete.controller;

import com.mbarca.vete.dto.request.OrderRequestDto;
import com.mbarca.vete.dto.request.SaleRequestDto;
import com.mbarca.vete.dto.response.OrderResponseDto;
import com.mbarca.vete.dto.response.SaleResponseDto;
import com.mbarca.vete.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<?> createOrderHandler (@RequestBody OrderRequestDto orderRequestDto) {
        try {
            String response = orderService.createOrder(orderRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getByDate")
    public ResponseEntity<?> getOrdersByDateHandler (
            @RequestParam("dateStart") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
            @RequestParam("dateEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd){
        try {
            List<OrderResponseDto> response = orderService.getOrdersByDate(dateStart, dateEnd);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getById/{orderId}")
    public ResponseEntity<?> getOrderHandler (@PathVariable Long orderId) {
        try {
            OrderResponseDto response = orderService.getOrderById(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
