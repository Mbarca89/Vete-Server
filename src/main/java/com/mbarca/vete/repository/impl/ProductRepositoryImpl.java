package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.Category;
import com.mbarca.vete.domain.Product;
import com.mbarca.vete.repository.ProductRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final String CREATE_PRODUCT = "INSERT INTO products (name, cost, price, stock, category_id, category_name, image, seller, provider) VALUES (?,?,?,?,?,?,?,?,?)";
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

        Category category = jdbcTemplate.queryForObject(GET_CATEGORY, params, types, new CategoryRowMapper());

        return jdbcTemplate.update(CREATE_PRODUCT,
                product.getName(),
                product.getCost(),
                product.getPrice(),
                product.getStock(),
                category.getId(),
                category.getName(),
                product.getImage(),
                product.getSeller(),
                product.getProvider()
                );
    }

    @Override
    public List<Product> getAllProducts () {
        return jdbcTemplate.query(GET_ALL_PRODUCTS, new ProductRowMapper());
    }

    static class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            Category category = new Category();
            category.setId(rs.getLong("id"));
            category.setName(rs.getString("name").toLowerCase());
            return category;
        }
    }

    static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getLong("id"));
            product.setName(rs.getString("name"));
            product.setCost(rs.getDouble("cost"));
            product.setPrice(rs.getDouble("price"));
            product.setStock(rs.getInt("stock"));
            product.setImage(rs.getBytes("image"));
            product.setCategoryName(rs.getString("category_name"));
            product.setProvider(rs.getString("provider"));
            return product;
        }
    }

}
