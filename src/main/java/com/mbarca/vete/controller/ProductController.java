package com.mbarca.vete.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbarca.vete.domain.PaginatedResults;
import com.mbarca.vete.domain.Product;
import com.mbarca.vete.dto.request.ProductRequestDto;
import com.mbarca.vete.dto.response.ProductResponseDto;
import com.mbarca.vete.dto.response.StockAlertResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.service.ProductService;
import com.mbarca.vete.utils.ImageCompressor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
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
    public ResponseEntity<String> createProductHandler(@RequestParam(value = "file", required = false) MultipartFile file,
                                                       @RequestParam("product") String productJson) {
        try {
            ProductRequestDto productRequestDto = new ObjectMapper().readValue(productJson, ProductRequestDto.class);
            byte[] compressedImage = null;
            if (file != null && !file.isEmpty()) {
                compressedImage = ImageCompressor.compressImage(file.getBytes());
            }
            String response = productService.createProduct(productRequestDto, compressedImage);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error" + e.getMessage());
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error json " + e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Imagen no soportada");
        } catch (MaxUploadSizeExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La imagen es demasiado grande");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());

        }
    }

    @CrossOrigin
    @GetMapping("/getProducts")
    public ResponseEntity<?> getAllProductsHandler() {
        try {
            List<ProductResponseDto> products = productService.getAllProducts();
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (BadSqlGrammarException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error sql: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getByCategory")
    public ResponseEntity<?> getByCategoryHandler(@RequestParam String categoryName,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "12") int size) {
        try {
            PaginatedResults<ProductResponseDto> products = productService.getByCategory(categoryName, page, size);
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getProductCount")
    public ResponseEntity<?> getProductCountHandler() {
        try {
            int count = productService.getProductCount();
            return ResponseEntity.status(HttpStatus.OK).body(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getCategoryCount")
    public ResponseEntity<?> getCategoryCountHandler(@RequestParam String categoryName) {
        try {
            int count = productService.getCategoryCount(categoryName);
            return ResponseEntity.status(HttpStatus.OK).body(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }
    @CrossOrigin
    @GetMapping("/getProductsPaginated")
    public ResponseEntity<?> getProductsPaginatedHandler(@RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "12") int size) {
        try {
            PaginatedResults<ProductResponseDto> products = productService.getProductsPaginated(page, size);
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/edit")
    public ResponseEntity<?> editProductHandler(@RequestParam(value = "file", required = false) MultipartFile file,
                                                @RequestParam("product") String productJson) {
        try {
            ProductRequestDto productRequestDto = new ObjectMapper().readValue(productJson, ProductRequestDto.class);
            byte[] compressedImage = null;
            if (file != null && !file.isEmpty()) {
                compressedImage = productService.compressImage(file.getBytes());
            }
            String response = productService.editProduct(productRequestDto, compressedImage);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (MissingDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en BD: " + e.getMessage());
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Json: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Imagen no soportada!");
        } catch (MaxUploadSizeExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La imagen es demasiado grande!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error: " + e.getMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProductHandler(@RequestParam Long productId) {
        try {
            String response = productService.deleteProduct(productId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/searchProduct")
    public ResponseEntity<?> searchProductHandler(@RequestParam String searchTerm) {
        try {
            List<ProductResponseDto> products = productService.searchProduct(searchTerm);
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getFromProvider")
    public ResponseEntity<?> getProductsFromProviderHandler(@RequestParam Long providerId) {
        try {
            List<ProductResponseDto> products = productService.getProductsFromProvider(providerId);
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getById")
    public ResponseEntity<?> getProductByIdHandler(@RequestParam Long productId) {
        try {
            ProductResponseDto product = productService.getProductById(productId);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/getStockAlerts")
    public ResponseEntity<?> getStockAlertsHandler() {
        try {
            List<StockAlertResponseDto> response = productService.getStockAlerts();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error:" + e.getMessage());
        }
    }
}
