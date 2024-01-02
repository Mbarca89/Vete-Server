package com.mbarca.vete.controller;

import com.mbarca.vete.dto.request.ProductRequestDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.service.ProductService;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProductHandler(@RequestBody ProductRequestDto productRequestDto) {
        try {
            String response = productService.createProduct(productRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( "error" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }
}
