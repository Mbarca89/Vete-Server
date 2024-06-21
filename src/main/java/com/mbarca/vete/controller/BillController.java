package com.mbarca.vete.controller;

import com.mbarca.vete.domain.Bill;
import com.mbarca.vete.service.BillService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bills")
public class BillController {
    BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @CrossOrigin
    @GetMapping("/getBillById")
    public ResponseEntity<?> getBillByIdHandler(@RequestParam Long id) {
        try {
            Bill response = billService.getByllById(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getBillByDate")
    public ResponseEntity<?> getBillByDateHandler(@RequestParam("dateStart") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
                                                  @RequestParam("dateEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd) {
        try {
            List<Bill> response = billService.getBillsByDate(dateStart,dateEnd);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
