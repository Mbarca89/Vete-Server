package com.mbarca.vete.service;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.mbarca.vete.domain.PaginatedResults;
import com.mbarca.vete.dto.request.ProductRequestDto;
import com.mbarca.vete.dto.response.ProductResponseDto;
import com.mbarca.vete.dto.response.StockAlertResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.exceptions.NotFoundException;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    String createProduct(ProductRequestDto productRequestDto, byte[] compressedImage) throws MissingDataException;
    List<ProductResponseDto> getAllProducts ();
    PaginatedResults<ProductResponseDto> getByCategory (String categoryName, int page, int size);
    Integer getProductCount ();
    Integer getCategoryCount (String categoryName);
    PaginatedResults<ProductResponseDto> getProductsPaginated(int page, int size);
    byte[] compressImage(byte[] imageData) throws IOException, ImageProcessingException, MetadataException;
    String editProduct(ProductRequestDto productRequestDto, byte[] compressedImage) throws Exception;
    String deleteProduct (Long productId) throws Exception;
    List<ProductResponseDto> searchProduct (String searchTerm);
    List<ProductResponseDto> getProductsFromProvider (Long providerId);
    ProductResponseDto getProductById (Long productId) throws NotFoundException;
    List<StockAlertResponseDto> getStockAlerts ();
}
