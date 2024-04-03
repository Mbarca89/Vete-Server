package com.mbarca.vete.service.impl;

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
}
