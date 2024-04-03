package com.mbarca.vete.controller;

import com.mbarca.vete.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
