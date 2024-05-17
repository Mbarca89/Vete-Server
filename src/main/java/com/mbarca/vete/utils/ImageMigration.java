package com.mbarca.vete.utils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ImageMigration {
    static JdbcTemplate jdbcTemplate;

    public ImageMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void migrateImages() {
        String selectQuery = "SELECT id, image FROM products";
        jdbcTemplate.query(selectQuery, (resultSet) -> {
            int id = resultSet.getInt("id");
            byte[] imageData = resultSet.getBytes("image");
            if (imageData != null && imageData.length > 0) {
                try {
                    byte[] compressedImageData = ImageCompressor.compressImage(imageData);
                    updateProductWithCompressedImage(id, compressedImageData);
                } catch (Exception e) {
                    throw new RuntimeException("Error compressing image for product ID: " + id, e);
                }
            }
        });
    }

    private static void updateProductWithCompressedImage(int id, byte[] compressedImageData) {
        String updateQuery = "UPDATE products SET new_image = ? WHERE id = ?";
        jdbcTemplate.update(updateQuery, compressedImageData, id);
    }
}
