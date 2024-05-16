package com.mbarca.vete.controller;

import com.mbarca.vete.utils.ImageMigration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/migration")
public class MigrationController {

    @CrossOrigin
    @PostMapping("/migrate")
    public ResponseEntity<String> migrationHandler() {
        try {
            ImageMigration.migrateImages();
            return ResponseEntity.status(HttpStatus.OK).body("logrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
