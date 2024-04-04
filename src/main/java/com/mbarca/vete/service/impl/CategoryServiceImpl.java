package com.mbarca.vete.service.impl;

import com.mbarca.vete.dto.request.CategoryRequestDto;
import com.mbarca.vete.exceptions.CategoryNotEmptyException;
import com.mbarca.vete.repository.CategoryRepository;
import com.mbarca.vete.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {

    public final CategoryRepository categoryRepository;

    public CategoryServiceImpl (CategoryRepository categoryRepository) {this.categoryRepository = categoryRepository;}
    @Override
    public List<String> getCategoriesNames() {
        return categoryRepository.getCategoriesNames();
    }

    @Override
    public String createCategory(CategoryRequestDto categoryRequestDto) {
        int response = categoryRepository.createCategory(categoryRequestDto.getName());
        if (response == 0) {
            return "Error al crear la categoría";
        }
        return "Categoria creada correctamente";
    }

    @Override
    public String deleteCategory(CategoryRequestDto categoryRequestDto) throws CategoryNotEmptyException {
        int response = categoryRepository.deleteCategory(categoryRequestDto.getName());
        if (response == 0) {
            return "Error al eliminar la categoría";
        }
        return "Categoría eliminada correctamente";
    }
}
