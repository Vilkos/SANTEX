package com.santex.service.implementation;

import com.santex.dto.SearchCriteriaAdminProduct;
import com.santex.dao.CategoryDao;
import com.santex.dao.SubcategoryDao;
import com.santex.entity.Category;
import com.santex.entity.Subcategory;
import com.santex.dto.Event;
import com.santex.service.SubcategoryService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Repository
@Transactional
public class SubcategoryServiceImpl implements SubcategoryService {
    private final SubcategoryDao subcategoryDao;
    private final CategoryDao categoryDao;
    private final ApplicationEventPublisher publisher;

    public SubcategoryServiceImpl(SubcategoryDao subcategoryDao, CategoryDao categoryDao, ApplicationEventPublisher publisher) {
        this.subcategoryDao = subcategoryDao;
        this.categoryDao = categoryDao;
        this.publisher = publisher;
    }

    @Override
    @CacheEvict(cacheNames = "menu", key = "'menu'")
    public void add(String subcategoryName, Category category) {
        Subcategory subcategory = new Subcategory(subcategoryName);
        subcategory.setCategory(category);
        subcategoryDao.save(subcategory);
        publisher.publishEvent(new Event());
    }

    @Override
    @CacheEvict(cacheNames = "menu", key = "'menu'")
    public void edit(int id, String subcategoryName, int category) {
        Subcategory subcategory = subcategoryDao.findOne(id);
        subcategory.setSubcategoryName(subcategoryName);
        subcategory.setCategory(categoryDao.findOne(category));
        subcategoryDao.save(subcategory);
        publisher.publishEvent(new Event());
    }

    @Override
    @CacheEvict(cacheNames = "menu", key = "'menu'")
    public void remove(int id) {
        subcategoryDao.delete(id);
        publisher.publishEvent(new Event());
    }

    @Override
    @Transactional(readOnly = true)
    public Subcategory findById(int id) {
        return subcategoryDao.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subcategory> findAll() {
        return subcategoryDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subcategory> findByCriteriaAdmin(SearchCriteriaAdminProduct criteriaAdmin) {
        return subcategoryDao.findAll(SubcategorySpecifications.byCriteriaAdmin(criteriaAdmin));
    }
}
