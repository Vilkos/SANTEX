package com.santex.service.implementation;

import com.santex.dao.CategoryDao;
import com.santex.dto.Event;
import com.santex.dto.SearchCriteriaAdminProduct;
import com.santex.entity.Category;
import com.santex.entity.Subcategory;
import com.santex.service.CategoryService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Repository
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao categoryDao;
    private final ApplicationEventPublisher publisher;

    public CategoryServiceImpl(CategoryDao categoryDao, ApplicationEventPublisher publisher) {
        this.categoryDao = categoryDao;
        this.publisher = publisher;
    }

    @Override
    @CacheEvict(cacheNames = "menu", key = "'menu'")
    public void add(String categoryName) {
        categoryDao.save(new Category(categoryName));
        publisher.publishEvent(new Event());
    }

    @Override
    @CacheEvict(cacheNames = "menu", key = "'menu'")
    public void edit(int id, String categoryName) {
        Category category = categoryDao.findOne(id);
        category.setCategoryName(categoryName);
        categoryDao.save(category);
        publisher.publishEvent(new Event());
    }

    @Override
    @CacheEvict(cacheNames = "menu", key = "'menu'")
    public void remove(int id) {
        categoryDao.delete(id);
        publisher.publishEvent(new Event());
    }

    @Override
    @Transactional(readOnly = true)
    public Category findById(int id) {
        return categoryDao.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findByCriteriaAdmin(SearchCriteriaAdminProduct criteriaAdmin) {
        return categoryDao.findAll(CategorySpecifications.byCriteriaAdmin(criteriaAdmin));
    }

    @Cacheable(cacheNames = "menu", key = "'menu'")
    public List<Category> createMenu() {
        List<Category> categories = findAll();
        for (Category category : categories) {
            List<Subcategory> subcategories = category.getSubcategoryList();
            subcategories.sort((o1, o2) -> {
                if (o1.getSubcategoryName().equals("Інше")) return 1;
                if (o2.getSubcategoryName().equals("Інше")) return -1;
                else return o1.getSubcategoryName().compareTo(o2.getSubcategoryName());
            });
            category.getSubcategoryList().removeIf(subcategory -> subcategory.getSubcategoryName().equals(category.getCategoryName()));
        }
        return categories;
    }
}