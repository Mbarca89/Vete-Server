package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.Category;
import com.mbarca.vete.domain.Product;
import com.mbarca.vete.domain.Provider;
import com.mbarca.vete.domain.StockAlert;
import com.mbarca.vete.exceptions.NotFoundException;
import com.mbarca.vete.repository.ProductRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final String CREATE_PRODUCT = "INSERT INTO products (name, bar_code, description, cost, price, stock, category_name, image, provider_name) VALUES (?,?,?,?,?,?,?,?,?)";
    private final String GET_ALL_PRODUCTS = "SELECT * FROM Products";
    private final String GET_PRODUCTS_BY_CATEGORY = "SELECT * FROM products WHERE category_name = ? LIMIT ? OFFSET ?";
    private final String GET_PRODUCT_COUNT = "SELECT COUNT(*) FROM products";
    private final String GET_CATEGORY_COUNT = "SELECT COUNT(*) FROM products WHERE category_name = ?";
    private final String GET_PRODUCTS_PAGINATED = "SELECT * FROM products LIMIT ? OFFSET ?";
    private final String GET_CATEGORY = "SELECT * FROM Category WHERE name = ?";
    private final String GET_PROVIDER = "SELECT * FROM providers WHERE name = ?";
    private final String RELATE_PRODUCT_PROVIDER = "INSERT INTO ProductProviders (product_id, provider_id) VALUES (?, ?)";
    private final String RELATE_PRODUCT_CATEGORY = "INSERT INTO ProductCategories (product_id, category_id) VALUES (?, ?)";
    private final String GET_PRODUCT_BY_ID = "SELECT * FROM products WHERE id= ?";
    private final String EDIT_PRODUCT = "UPDATE products SET name = ?, bar_code = ?, description = ?, cost = ?, price = ?, stock = ?, category_name = ?, image = ?, provider_name = ?, stock_alert = ?, published = ? WHERE id = ?";
    private final String DELETE_PRODUCT = "DELETE FROM products WHERE id = ?";
    private final String DELETE_PRODUCT_CATEGORY = "DELETE FROM ProductCategories WHERE product_id = ?";
    private final String DELETE_PRODUCT_PROVIDER = "DELETE FROM ProductProviders WHERE product_id = ?";
    private final String GET_PRODUCT_BY_NAME = "SELECT * FROM Products WHERE LOWER(name) LIKE LOWER(?)";
    private final String GET_PRODUCT_BY_BARCODE = "SELECT * FROM Products WHERE bar_code = ?";
    private final String GET_STOCK_ALERTS = "SELECT * FROM Products WHERE stock_alert = true AND stock <= 5";
    private final String GET_PRODUCTS_FROM_PROVIDERS = "SELECT p.id, p.name, p.description, p.category_name, p.provider_name, p.bar_code, p.cost, p.price, p.stock " +
            "FROM Products p " +
            "JOIN ProductProviders pp ON p.id = pp.product_id " +
            "WHERE pp.provider_id = ?";
    private final JdbcTemplate jdbcTemplate;

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createProduct(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        Object[] params = {product.getCategoryName()};
        int[] types = {1};
        Category category = jdbcTemplate.queryForObject(GET_CATEGORY, params, types, new CategoryRowMapper());



        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(CREATE_PRODUCT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getName());
            if (product.getBarCode() != 0) {
                ps.setDouble(2, product.getBarCode());
            } else {
                ps.setNull(2, java.sql.Types.DOUBLE);
            }
            ps.setString(3, product.getDescription());
            ps.setDouble(4, product.getCost());
            ps.setDouble(5, product.getPrice());
            ps.setDouble(6, product.getStock());
            ps.setString(7, category.getName());
            ps.setBytes(8, product.getPhoto());
            ps.setString(9, product.getProviderName());
            return ps;
        }, keyHolder);
        Long productId = keyHolder.getKey().longValue();

        if(!Objects.equals(product.getProviderName(), "Ninguno")) {
            Object[] params2 = {product.getProviderName()};
            Provider provider = jdbcTemplate.queryForObject(GET_PROVIDER, params2, types, new ProviderRepositoryImpl.ProviderRowMapper());
            jdbcTemplate.update(RELATE_PRODUCT_PROVIDER, productId, provider.getId());
        }

        return jdbcTemplate.update(RELATE_PRODUCT_CATEGORY, productId, category.getId());
    }

    @Override
    public List<Product> getAllProducts() {
        System.out.println(GET_ALL_PRODUCTS);
        return jdbcTemplate.query(GET_ALL_PRODUCTS, new ProductRowMapper(true));
    }

    @Override
    public List<Product> getByCategory(String categoryName, int limit, int offset) {
        return jdbcTemplate.query(GET_PRODUCTS_BY_CATEGORY, new Object[]{categoryName, limit, offset}, new ProductRowMapper(true));
    }

    @Override
    public Integer getProductCount() {
        return jdbcTemplate.queryForObject(GET_PRODUCT_COUNT, Integer.class);
    }

    @Override
    public Integer getCategoryCount(String categoryName) {
        return jdbcTemplate.queryForObject(GET_CATEGORY_COUNT, new Object[]{categoryName}, Integer.class);
    }

    @Override
    public List<Product> getProductsPaginated(int limit, int offset) {
        return jdbcTemplate.query(GET_PRODUCTS_PAGINATED, new Object[]{limit, offset}, new ProductRowMapper(true));
    }

    @Override
    public Integer editProduct(Product newProduct) throws NotFoundException {
        Object[] params = {newProduct.getId()};
        int [] types = {1};
        Product currentProduct = jdbcTemplate.queryForObject(GET_PRODUCT_BY_ID, params, types, new ProductRowMapper(true));
        if(currentProduct == null) {
            throw new NotFoundException("Producto no encontrado!");
        }

        Product editProduct = getEditProduct(newProduct, currentProduct);
        return jdbcTemplate.update(EDIT_PRODUCT,
                editProduct.getName(),
                editProduct.getBarCode(),
                editProduct.getDescription(),
                editProduct.getCost(),
                editProduct.getPrice(),
                editProduct.getStock(),
                editProduct.getCategoryName(),
                editProduct.getPhoto(),
                editProduct.getProviderName(),
                editProduct.getStockAlert(),
                editProduct.getPublished(),
                currentProduct.getId());
    }

    @Override
    public Integer deleteProduct (Long productId) {
        jdbcTemplate.update(DELETE_PRODUCT_CATEGORY, productId);
        jdbcTemplate.update(DELETE_PRODUCT_PROVIDER, productId);
        return jdbcTemplate.update(DELETE_PRODUCT, productId);
    }
    @Override
    public List<Product> searchProduct (String searchTerm) {
        try {
            Double barCode = Double.parseDouble(searchTerm);
            return jdbcTemplate.query(GET_PRODUCT_BY_BARCODE, new Object[]{barCode}, new ProductRowMapper(true));
        } catch (NumberFormatException e) {
            String searchTermOk = "%" + searchTerm + "%";
            return jdbcTemplate.query(GET_PRODUCT_BY_NAME, new Object[] {searchTermOk}, new ProductRowMapper(true));
        }
    }

    @Override
    public List<Product> getProductsFromProvider (Long providerId) {
        return jdbcTemplate.query(GET_PRODUCTS_FROM_PROVIDERS, new Object[]{providerId}, new ProductRowMapper(false));
    }

    @Override
    public Product getProductById (Long productId) throws NotFoundException {
        Object[] params = {productId};
        int [] types = {1};
        Product currentProduct = jdbcTemplate.queryForObject(GET_PRODUCT_BY_ID, params, types, new ProductRowMapper(true));
        if(currentProduct == null) {
            throw new NotFoundException("Producto no encontrado!");
        } else {
            return currentProduct;
        }
    }

    @Override
    public List<StockAlert> getStockAlerts () {
        return jdbcTemplate.query(GET_STOCK_ALERTS, new StockAlertRowMapper());
    }

    private static Product getEditProduct(Product newProduct, Product currentProduct) {
        Product editProduct = new Product();
        editProduct.setName(!Objects.equals(newProduct.getName(), "") ? newProduct.getName() : currentProduct.getName());
        editProduct.setDescription(!Objects.equals(newProduct.getDescription(), "") ? newProduct.getDescription() : currentProduct.getDescription());
        editProduct.setBarCode(newProduct.getBarCode() != null ? newProduct.getBarCode() : currentProduct.getBarCode());
        editProduct.setStock(newProduct.getStock() != null ? newProduct.getStock() : currentProduct.getStock());
        editProduct.setCost(newProduct.getCost() != null ? newProduct.getCost() : currentProduct.getCost());
        editProduct.setPrice(newProduct.getPrice() != null ? newProduct.getPrice() : currentProduct.getPrice());
        editProduct.setCategoryName(!Objects.equals(newProduct.getCategoryName(), "") ? newProduct.getCategoryName() : currentProduct.getCategoryName());
        editProduct.setProviderName(!Objects.equals(newProduct.getProviderName(), "") ? newProduct.getProviderName() : currentProduct.getProviderName());
        editProduct.setPhoto(newProduct.getPhoto() != null ? newProduct.getPhoto() : currentProduct.getPhoto());
        editProduct.setStockAlert(newProduct.getStockAlert());
        editProduct.setPublished(newProduct.getPublished());
        return editProduct;
    }

    static class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            Category category = new Category();
            category.setId(rs.getLong("id"));
            category.setName(rs.getString("name"));
            return category;
        }
    }

    static class StockAlertRowMapper implements RowMapper<StockAlert> {
        @Override
        public StockAlert mapRow(ResultSet rs, int rowNum) throws SQLException {
            StockAlert stockAlert = new StockAlert();
            stockAlert.setProductName(rs.getString("name"));
            stockAlert.setStock(rs.getInt("stock"));
            return stockAlert;
        }
    }

    static class ProductRowMapper implements RowMapper<Product> {

        private boolean includeImage;

        public ProductRowMapper(boolean includeImage) {
            this.includeImage = includeImage;
        }
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getLong("id"));
            product.setName(rs.getString("name"));
            product.setDescription(rs.getString("description"));
            product.setCategoryName(rs.getString("category_name"));
            product.setBarCode(rs.getDouble("bar_code"));
            product.setCost(rs.getDouble("cost"));
            product.setPrice(rs.getDouble("price"));
            product.setStock(rs.getInt("stock"));
            product.setProviderName(rs.getString("provider_name"));
            product.setStockAlert(rs.getBoolean("stock_alert"));
            product.setPublished(rs.getBoolean("published"));
            if(includeImage) product.setPhoto(rs.getBytes("image"));
            return product;
        }
    }

}
