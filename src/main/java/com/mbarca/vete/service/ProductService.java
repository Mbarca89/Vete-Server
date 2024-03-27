package com.mbarca.vete.service;

import com.mbarca.vete.dto.request.ProductRequestDto;
import com.mbarca.vete.dto.response.ProductResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    String createProduct(ProductRequestDto productRequestDto, byte[] compressedImage) throws MissingDataException;
    List<ProductResponseDto> getAllProducts ();
    byte[] compressImage(byte[] imageData) throws IOException;
}
