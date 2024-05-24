package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.*;
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

    private final String CREATE_PRODUCT = "INSERT INTO products (name, bar_code, description, cost, price, stock, category_name, image, thumbnail, provider_name) VALUES (?,?,?,?,?,?,?,?,?,?)";
    private final String GET_PRODUCT_BY_ID = "SELECT * FROM products WHERE id= ?";
    private final JdbcTemplate jdbcTemplate;

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createProduct(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        Object[] params = {product.getCategoryName()};
        int[] types = {1};
        String GET_CATEGORY = "SELECT * FROM Category WHERE name = ?";
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
            assert category != null;
            ps.setString(7, category.getName());
            ps.setBytes(8, product.getImage());
            ps.setBytes(9, product.getThumbnail());
            ps.setString(10, product.getProviderName());
            return ps;
        }, keyHolder);
        Long productId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        if(!Objects.equals(product.getProviderName(), "Ninguno")) {
            Object[] params2 = {product.getProviderName()};
            String GET_PROVIDER = "SELECT * FROM providers WHERE name = ?";
            Provider provider = jdbcTemplate.queryForObject(GET_PROVIDER, params2, types, new ProviderRepositoryImpl.ProviderRowMapper());
            String RELATE_PRODUCT_PROVIDER = "INSERT INTO ProductProviders (product_id, provider_id) VALUES (?, ?)";
            assert provider != null;
            jdbcTemplate.update(RELATE_PRODUCT_PROVIDER, productId, provider.getId());
        }

        String RELATE_PRODUCT_CATEGORY = "INSERT INTO ProductCategories (product_id, category_id) VALUES (?, ?)";
        assert category != null;
        return jdbcTemplate.update(RELATE_PRODUCT_CATEGORY, productId, category.getId());
    }

    @Override
    public List<Product> getAllProducts() {
        String GET_ALL_PRODUCTS = "SELECT * FROM Products";
        return jdbcTemplate.query(GET_ALL_PRODUCTS, new ProductRowMapper(true, true));
    }

    @Override
    public PaginatedResults<Product> getByCategory(String categoryName, int limit, int offset) {
        String GET_CATEGORY_COUNT = "SELECT COUNT(*) FROM products WHERE category_name = ?";
        String GET_PRODUCTS_BY_CATEGORY = "SELECT * FROM products WHERE category_name = ? LIMIT ? OFFSET ?";
        Integer totalCount = jdbcTemplate.queryForObject(GET_CATEGORY_COUNT, new Object[]{categoryName}, Integer.class);
        List<Product> products = jdbcTemplate.query(GET_PRODUCTS_BY_CATEGORY, new Object[]{categoryName, limit, offset}, new ProductRowMapper(false, true));

        return new PaginatedResults<Product>(products, totalCount != null ? totalCount:0);
          }

    @Override
    public Integer getProductCount() {
        String GET_PRODUCT_COUNT = "SELECT COUNT(*) FROM products";
        return jdbcTemplate.queryForObject(GET_PRODUCT_COUNT, Integer.class);
    }

    @Override
    public Integer getCategoryCount(String categoryName) {
        String GET_CATEGORY_COUNT = "SELECT COUNT(*) FROM products WHERE category_name = ?";
        return jdbcTemplate.queryForObject(GET_CATEGORY_COUNT, new Object[]{categoryName}, Integer.class);
    }

    @Override
    public PaginatedResults<Product> getProductsPaginated(int limit, int offset) {
        String GET_PRODUCTS_PAGINATED = "SELECT * FROM products LIMIT ? OFFSET ?";
        String GET_PRODUCT_COUNT = "SELECT COUNT(*) FROM products";

        Integer totalCount = jdbcTemplate.queryForObject(GET_PRODUCT_COUNT, Integer.class);
        List<Product> products = jdbcTemplate.query(GET_PRODUCTS_PAGINATED, new Object[]{limit, offset}, new ProductRowMapper(false, true));

        return new PaginatedResults<Product>(products, totalCount != null ? totalCount:0);
    }

    @Override
    public Integer editProduct(Product newProduct) throws NotFoundException {
        Object[] params = {newProduct.getId()};
        int [] types = {1};
        Product currentProduct = jdbcTemplate.queryForObject(GET_PRODUCT_BY_ID, params, types, new ProductRowMapper(true, true));
        if(currentProduct == null) {
            throw new NotFoundException("Producto no encontrado!");
        }

        Product editProduct = getEditProduct(newProduct, currentProduct);
        String EDIT_PRODUCT = "UPDATE products SET name = ?, bar_code = ?, description = ?, cost = ?, price = ?, stock = ?, category_name = ?, image = ?, thumbnail = ?, provider_name = ?, stock_alert = ?, published = ? WHERE id = ?";
        return jdbcTemplate.update(EDIT_PRODUCT,
                editProduct.getName(),
                editProduct.getBarCode(),
                editProduct.getDescription(),
                editProduct.getCost(),
                editProduct.getPrice(),
                editProduct.getStock(),
                editProduct.getCategoryName(),
                editProduct.getImage(),
                editProduct.getThumbnail(),
                editProduct.getProviderName(),
                editProduct.getStockAlert(),
                editProduct.getPublished(),
                currentProduct.getId());
    }

    @Override
    public Integer deleteProduct (Long productId) {
        String DELETE_PRODUCT_CATEGORY = "DELETE FROM ProductCategories WHERE product_id = ?";
        jdbcTemplate.update(DELETE_PRODUCT_CATEGORY, productId);
        String DELETE_PRODUCT_PROVIDER = "DELETE FROM ProductProviders WHERE product_id = ?";
        jdbcTemplate.update(DELETE_PRODUCT_PROVIDER, productId);
        String DELETE_PRODUCT = "DELETE FROM products WHERE id = ?";
        return jdbcTemplate.update(DELETE_PRODUCT, productId);
    }
    @Override
    public List<Product> searchProduct (String searchTerm) {
        try {
            double barCode = Double.parseDouble(searchTerm);
            String GET_PRODUCT_BY_BARCODE = "SELECT * FROM Products WHERE bar_code = ?";
            return jdbcTemplate.query(GET_PRODUCT_BY_BARCODE, new Object[]{barCode}, new ProductRowMapper(false, false));
        } catch (NumberFormatException e) {
            String searchTermOk = "%" + searchTerm + "%";
            String GET_PRODUCT_BY_NAME = "SELECT * FROM Products WHERE LOWER(name) LIKE LOWER(?)";
            return jdbcTemplate.query(GET_PRODUCT_BY_NAME, new Object[] {searchTermOk}, new ProductRowMapper(false, false));
        }
    }

    @Override
    public List<Product> getProductsFromProvider (Long providerId) {
        String GET_PRODUCTS_FROM_PROVIDERS = "SELECT p.id, p.name, p.description, p.category_name, p.provider_name, p.bar_code, p.cost, p.price, p.stock " +
                "FROM Products p " +
                "JOIN ProductProviders pp ON p.id = pp.product_id " +
                "WHERE pp.provider_id = ?";
        return jdbcTemplate.query(GET_PRODUCTS_FROM_PROVIDERS, new Object[]{providerId}, new ProductRowMapper(false, false));
    }

    @Override
    public Product getProductById (Long productId) throws NotFoundException {
        Object[] params = {productId};
        int [] types = {1};
        Product currentProduct = jdbcTemplate.queryForObject(GET_PRODUCT_BY_ID, params, types, new ProductRowMapper(true, false));
        if(currentProduct == null) {
            throw new NotFoundException("Producto no encontrado!");
        } else {
            return currentProduct;
        }
    }

    @Override
    public List<StockAlert> getStockAlerts () {
        String GET_STOCK_ALERTS = "SELECT * FROM Products WHERE stock_alert = true AND stock <= 5";
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
        editProduct.setImage(newProduct.getImage() != null ? newProduct.getImage() : currentProduct.getImage());
        editProduct.setThumbnail(newProduct.getThumbnail() != null ? newProduct.getThumbnail() : currentProduct.getThumbnail());
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

        private final boolean includeImage;
        private final boolean includeThumbnail;

        public ProductRowMapper(boolean includeImage, boolean includeThumbnail) {
            this.includeImage = includeImage;
            this.includeThumbnail = includeThumbnail;
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
            if(includeImage) product.setImage(rs.getBytes("image"));
            if (includeThumbnail) product.setThumbnail(rs.getBytes("thumbnail"));
            return product;
        }
    }

}
