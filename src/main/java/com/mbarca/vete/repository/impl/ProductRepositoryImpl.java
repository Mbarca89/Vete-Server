package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.Category;
import com.mbarca.vete.domain.Product;
import com.mbarca.vete.repository.ProductRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final String CREATE_PRODUCT = "INSERT INTO products (name, cost, price, stock, category_id, category_name, seller) VALUES (?,?,?,?,?,?,?)";
    private final String GET_ALL_PRODUCTS = "SELECT * FROM products";
    private final String GET_CATEGORY = "SELECT * FROM Category WHERE name = ?";
    private final JdbcTemplate jdbcTemplate;

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createProduct(Product product) {
        Object[] params = {product.getCategoryName()};
        int[] types = {1};

        Category category = jdbcTemplate.queryForObject(GET_CATEGORY, params, types, new ExpenseCategoryRowMapper());

        return jdbcTemplate.update(CREATE_PRODUCT,
                product.getName(),
                product.getCost(),
                product.getPrice(),
                product.getStock(),
                category.getId(),
                category.getName(),
                product.getSeller()
                );
    }

    static class ExpenseCategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            Category category = new Category();
            category.setId(rs.getLong("id"));
            category.setName(rs.getString("name").toLowerCase());
            return category;
        }
    }

}
