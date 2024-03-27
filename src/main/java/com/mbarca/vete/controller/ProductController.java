package com.mbarca.vete.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbarca.vete.domain.Product;
import com.mbarca.vete.dto.request.ProductRequestDto;
import com.mbarca.vete.dto.response.ProductResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.service.ProductService;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<String> createProductHandler(@RequestParam("file") MultipartFile file,
                                                       @RequestParam("product") String productJson) {
        try {
            ProductRequestDto productRequestDto = new ObjectMapper().readValue(productJson, ProductRequestDto.class);
            byte[] compressedImage = productService.compressImage(file.getBytes());
            String response = productService.createProduct(productRequestDto, compressedImage);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error" + e.getMessage());
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error json" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }

    @CrossOrigin
    @GetMapping("/getProducts")
    public ResponseEntity<List<ProductResponseDto>> getAllProductsHandler() {
        try {
            List<ProductResponseDto> products = productService.getAllProducts();
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
