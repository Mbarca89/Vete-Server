package com.mbarca.vete.repository.impl;

import com.mbarca.vete.exceptions.CategoryNotEmptyException;
import com.mbarca.vete.repository.CategoryRepository;
import com.mbarca.vete.repository.ProductRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final String GET_CATEGORIES_NAMES = "SELECT name FROM Category";
    private final String GET_CATEGORIES_NAMES_FOR_WEB = "SELECT DISTINCT c.name " +
            "FROM Category c " +
            "JOIN ProductCategories pc ON c.id = pc.category_id " +
            "JOIN Products p ON pc.product_id = p.id " +
            "WHERE p.published = TRUE;";
    private final String CREATE_CATEGORY = "INSERT INTO Category (name) VALUES (?)";
    private final String DELETE_CATEGORY = "DELETE FROM Category WHERE name = ?";

    private final JdbcTemplate jdbcTemplate;
    ProductRepository productRepository;

    public CategoryRepositoryImpl(JdbcTemplate jdbcTemplate, ProductRepository productRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.productRepository = productRepository;
    }
    @Override
    public List<String> getCategoriesNames() {
        {
            return jdbcTemplate.queryForList(GET_CATEGORIES_NAMES, String.class);
        }
    }

    @Override
    public List<String> getCategoriesNamesForWeb() {
        {
            return jdbcTemplate.queryForList(GET_CATEGORIES_NAMES_FOR_WEB, String.class);
        }
    }

    public Integer createCategory(String name) {
        return jdbcTemplate.update(CREATE_CATEGORY, name);
    }

    public Integer deleteCategory(String name) throws CategoryNotEmptyException {
        int count = productRepository.getCategoryCount(name);
        if(count > 0) throw new CategoryNotEmptyException("La categoria contiene productos asociados");
        return jdbcTemplate.update(DELETE_CATEGORY, name);}
}
