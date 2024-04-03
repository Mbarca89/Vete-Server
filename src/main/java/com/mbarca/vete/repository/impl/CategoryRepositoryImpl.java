package com.mbarca.vete.repository.impl;

import com.mbarca.vete.repository.CategoryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    private final String GET_CATEGORIES_NAMES = "SELECT name FROM Category";

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<String> getCategoriesNames() {
        {
            return jdbcTemplate.queryForList(GET_CATEGORIES_NAMES, String.class);
        }
    }
}
