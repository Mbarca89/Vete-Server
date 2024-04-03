package com.mbarca.vete.repository;

import com.mbarca.vete.domain.Product;

import java.util.List;

public interface ProductRepository {
    Integer createProduct (Product product);
    List<Product> getAllProducts ();
    List<Product> getByCategory (String categoryName, int limit, int offset);
    Integer getProductCount ();
    Integer getCategoryCount (String categoryName);
    List<Product> getProductsPaginated (int limit, int offset);
}
