package com.mbarca.vete.controller;

import com.mbarca.vete.domain.MonthlyReport;
import com.mbarca.vete.dto.request.SaleRequestDto;
import com.mbarca.vete.dto.response.CategoryTotalResponseDto;
import com.mbarca.vete.dto.response.SaleResponseDto;
import com.mbarca.vete.service.SaleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
public class SaleController {
    SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<?> createSaleHandler (@RequestBody SaleRequestDto saleRequestDto) {
        try {
            String response = saleService.createSale(saleRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getById/{saleId}")
    public ResponseEntity<?> getSaleHandler (@PathVariable Long saleId) {
        try {
            SaleResponseDto response = saleService.getSaleById(saleId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getByDate")
    public ResponseEntity<?> getSalesByDateHandler (
            @RequestParam("dateStart") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
            @RequestParam("dateEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd){
        try {
            List<SaleResponseDto> response = saleService.getSalesByDate(dateStart, dateEnd);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getByMonth")
    public ResponseEntity<?> getSalesByMonthHandler (
            @RequestParam("dateStart") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
            @RequestParam("dateEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd){
        try {
            MonthlyReport response = saleService.getSalesReport(dateStart, dateEnd);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getByCategory")
    public ResponseEntity<?> getSalesByCategoryHandler (@RequestParam("dateStart") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
                                                        @RequestParam("dateEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd){
        try {
            List<CategoryTotalResponseDto> response = saleService.getSalesByCategory(dateStart, dateEnd);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
