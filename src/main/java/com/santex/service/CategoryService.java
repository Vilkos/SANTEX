package com.santex.service;

import com.santex.dto.SearchCriteriaAdminProduct;
import com.santex.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    void add(String categoryName);

    void edit(int id, String categoryName);

    void remove(int id);

    Optional<Category> findById(int id);

    List<Category> findAll();

    List<Category> createMenu();

    List<Category> findByCriteriaAdmin(SearchCriteriaAdminProduct criteriaAdmin);
}
