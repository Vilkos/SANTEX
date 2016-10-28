package com.santex.service;

import com.santex.dto.SearchCriteriaAdminProduct;
import com.santex.entity.Category;
import com.santex.entity.Subcategory;

import java.util.List;

public interface SubcategoryService {

    void add(String subcategoryName, Category category);

    void edit(int id, String subcategoryName, int category);

    void remove(int id);

    Subcategory findById(int id);

    List<Subcategory> findAll();

    List<Subcategory> findByCriteriaAdmin(SearchCriteriaAdminProduct criteriaAdmin);

}
