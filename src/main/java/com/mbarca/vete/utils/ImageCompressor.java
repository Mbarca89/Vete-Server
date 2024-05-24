package com.mbarca.vete.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.mbarca.vete.domain.Images;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.Buffer;

public class ImageCompressor {

    public static Images compressImage(byte[] imageData) throws Exception {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));

        // Rotate the image based on EXIF orientation
        BufferedImage rotatedImage = rotateImage(originalImage, imageData);

        // Resize the rotated image to 1280x720 or 720x1280
        BufferedImage resizedImage = resizeImage(rotatedImage, 1280, 720);
        BufferedImage resizedThumbnail = resizeImage(rotatedImage, 320, 184);
        // Convert resized image to WebP

        byte [] fullImage = convertToWebP(resizedImage);
        byte [] thumbnail = convertToWebP(resizedThumbnail);
        return new Images(fullImage, thumbnail);
    }

    private static BufferedImage rotateImage(BufferedImage originalImage, byte[] imageData) throws Exception {
        int orientation = 1; // Default orientation (no rotation)
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(imageData));
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        } catch (Exception e) {
            throw new Exception("Error al rotar la imagen: " + e.getMessage());
        }

        switch (orientation) {
            case 3:
                return Scalr.rotate(originalImage, Scalr.Rotation.CW_180);
            case 6:
                return Scalr.rotate(originalImage, Scalr.Rotation.CW_90);
            case 8:
                return Scalr.rotate(originalImage, Scalr.Rotation.CW_270);
            default:
                return originalImage; // No rotation needed
        }
    }

    private static BufferedImage resizeImage(BufferedImage image, int targetWidth, int targetHeight) {
        return Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight);
    }

    private static byte[] convertToWebP(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}
