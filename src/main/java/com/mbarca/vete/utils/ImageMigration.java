package com.mbarca.vete.utils;

import com.mbarca.vete.domain.Images;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ImageMigration {
    static JdbcTemplate jdbcTemplate;

    public ImageMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void migrateImages() {
        String selectQuery = "SELECT id, image FROM Products";
        jdbcTemplate.query(selectQuery, (resultSet) -> {
            int id = resultSet.getInt("id");
            byte[] imageData = resultSet.getBytes("image");
            if (imageData != null && imageData.length > 0) {
                try {
                    Images images = ImageCompressor.compressImage(imageData);
                    updateProductWithCompressedImage(id, images);
                } catch (Exception e) {
                    throw new RuntimeException("Error compressing image for product ID: " + id, e);
                }
            }
        });
    }

    private static void updateProductWithCompressedImage(int id, Images images) {
        String updateQuery = "UPDATE products SET thumbnail = ? WHERE id = ?";
        jdbcTemplate.update(updateQuery, images.getThumbnail(), id);
    }
}
