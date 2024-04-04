package com.mbarca.vete.service;

import com.mbarca.vete.dto.request.CategoryRequestDto;
import com.mbarca.vete.exceptions.CategoryNotEmptyException;

import java.util.List;

public interface CategoryService {
    List<String> getCategoriesNames ();
    String createCategory(CategoryRequestDto categoryRequestDto);
    String deleteCategory(CategoryRequestDto categoryRequestDto) throws CategoryNotEmptyException;
}
