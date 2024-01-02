package com.mbarca.vete.service;

import com.mbarca.vete.dto.request.ProductRequestDto;
import com.mbarca.vete.exceptions.MissingDataException;

public interface ProductService {
    String createProduct (ProductRequestDto productRequestDto) throws MissingDataException;
}
