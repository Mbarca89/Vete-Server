package com.mbarca.vete.repository;

import com.mbarca.vete.domain.PaginatedResults;
import com.mbarca.vete.domain.Product;
import com.mbarca.vete.domain.StockAlert;
import com.mbarca.vete.exceptions.NotFoundException;

import java.util.List;

public interface ProductRepository {
    Integer createProduct (Product product);
    List<Product> getAllProducts ();
    PaginatedResults<Product> getByCategory (String categoryName, int limit, int offset);
    Integer getProductCount ();
    Integer getCategoryCount (String categoryName);
    PaginatedResults<Product> getProductsPaginated (int limit, int offset);
    Integer editProduct (Product newProduct) throws NotFoundException;
    Integer deleteProduct (Long productId);
    List<Product> searchProduct (String searchTerm);
    List<Product> getProductsFromProvider (Long providerId);
    Product getProductById (Long productId) throws NotFoundException;
    List<StockAlert> getStockAlerts ();
}
