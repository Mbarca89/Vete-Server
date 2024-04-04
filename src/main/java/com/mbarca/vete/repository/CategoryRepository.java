package com.mbarca.vete.repository;

import com.mbarca.vete.exceptions.CategoryNotEmptyException;

import java.util.List;

public interface CategoryRepository {
    List<String> getCategoriesNames ();
    Integer createCategory (String name);
    Integer deleteCategory (String name) throws CategoryNotEmptyException;
}
