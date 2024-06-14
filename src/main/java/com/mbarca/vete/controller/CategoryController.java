package com.mbarca.vete.controller;

import com.mbarca.vete.dto.request.CategoryRequestDto;
import com.mbarca.vete.exceptions.CategoryNotEmptyException;
import com.mbarca.vete.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    public final CategoryService categoryService;

    public CategoryController (CategoryService categoryService) {this.categoryService = categoryService;}

    @CrossOrigin
    @GetMapping("/getCategoriesNames")
    public ResponseEntity<?> getCategoriesNamesHandler() {
        try {
            List<String> categories = categoryService.getCategoriesNames();
            return ResponseEntity.status(HttpStatus.OK).body(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/public/getCategoriesNamesForWeb")
    public ResponseEntity<?> getCategoriesNamesForWebHandler() {
        try {
            List<String> categories = categoryService.getCategoriesNamesForWeb();
            return ResponseEntity.status(HttpStatus.OK).body(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<?> creteCategoryHandler(@RequestBody CategoryRequestDto categoryRequestDto) {
        try {
            String response = categoryService.createCategory(categoryRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategoryHandler(@RequestParam String name) {
        try {
            CategoryRequestDto categoryRequestDto = new CategoryRequestDto(name);
            String response = categoryService.deleteCategory(categoryRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (CategoryNotEmptyException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}
