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

    private final String CREATE_PRODUCT = "INSERT INTO products (name, description, cost, price, stock, category_id, category_name, image, seller, provider) VALUES (?,?,?,?,?,?,?,?,?,?)";
    private final String GET_ALL_PRODUCTS = "SELECT * FROM products";
    private final String GET_PRODUCTS_BY_CATEGORY = "SELECT * FROM products WHERE category_name = ? LIMIT ? OFFSET ?";
private final String GET_PRODUCT_COUNT = "SELECT COUNT(*) FROM products";
    private final String GET_CATEGORY_COUNT = "SELECT COUNT(*) FROM products WHERE category_name = ?";
    private final String GET_PRODUCTS_PAGINATED = "SELECT * FROM products LIMIT ? OFFSET ?";
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
                product.getDescription(),
                product.getCost(),
                product.getPrice(),
                product.getStock(),
                category.getId(),
                category.getName(),
                product.getPhoto(),
                product.getSeller(),
                product.getProvider()
                );
    }

    @Override
    public List<Product> getAllProducts () {
        return jdbcTemplate.query(GET_ALL_PRODUCTS, new ProductRowMapper());
    }

    @Override
    public List<Product> getByCategory (String categoryName, int limit, int offset) {
        return jdbcTemplate.query(GET_PRODUCTS_BY_CATEGORY, new Object[]{categoryName, limit, offset}, new ProductRowMapper());
    }
    @Override
    public Integer getProductCount () {
        return jdbcTemplate.queryForObject(GET_PRODUCT_COUNT, Integer.class);
    }
    @Override
    public Integer getCategoryCount (String categoryName) {
        return jdbcTemplate.queryForObject(GET_CATEGORY_COUNT, new Object[]{categoryName}, Integer.class);
    }
    @Override
    public List<Product> getProductsPaginated(int limit, int offset) {
        return jdbcTemplate.query(GET_PRODUCTS_PAGINATED, new Object[]{limit, offset}, new ProductRowMapper());
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
            product.setDescription(rs.getString("description"));
            product.setBarCode(rs.getDouble("bar_code"));
            product.setCost(rs.getDouble("cost"));
            product.setPrice(rs.getDouble("price"));
            product.setStock(rs.getInt("stock"));
            product.setPhoto(rs.getBytes("image"));
            product.setCategoryName(rs.getString("category_name"));
            product.setProvider(rs.getString("provider"));
            return product;
        }
    }

}
