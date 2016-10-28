package com.santex.service.implementation;

import com.santex.dto.ProductCustomerDto;
import com.santex.dto.SearchCriteria;
import com.santex.entity.Brand;
import com.santex.entity.Category;
import com.santex.entity.Subcategory;
import com.santex.service.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Service
public class CachePreheatServiceImpl implements CachePreheatService {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final SubcategoryService subcategoryService;
    private final BrandService brandService;

    public CachePreheatServiceImpl(ProductService productService, CategoryService categoryService, SubcategoryService subcategoryService, BrandService brandService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.subcategoryService = subcategoryService;
        this.brandService = brandService;
    }

    @Override
    @Async
    //@Scheduled(cron = "0 0 3 * * *", zone = "Europe/Kiev")
    public void run() {
        SearchCriteria criteria = new SearchCriteria();

        walkThroughPages(criteria);
        criteria.clearCriteria();

        List<Category> categories = categoryService.findAll();

        for (Category category : categories) {
            criteria.setCatId(category.getId());
            walkThroughPages(criteria);
            walkThroughBrands(criteria);
            walkThroughSorting(criteria);
        }

        criteria.clearCriteria();
        List<Subcategory> subcategories = subcategoryService.findAll();

        for (Subcategory subcategory : subcategories) {
            criteria.setSubId(subcategory.getId());
            walkThroughPages(criteria);
            walkThroughBrands(criteria);
            walkThroughSorting(criteria);
        }

        criteria.clearCriteria();
    }

    private void walkThroughPages(SearchCriteria criteria) {
        int num = 1;
        int totalPages = 1;
        PageRequest request;
        for (int i = 0; i < totalPages; i++) {
            request = new PageRequest(num - 1, 20);
            Page<ProductCustomerDto> productCustomerDtos = productService.findByCriteriaCustomer(criteria, request);
            num = i + 1;
            totalPages = productCustomerDtos.getTotalPages();
        }
    }

    private void walkThroughBrands(SearchCriteria criteria) {
        List<Brand> brands = brandService.findByCriteriaCustomer(criteria);
        for (Brand brand : brands) {
            criteria.setBrandId(brand.getId());
            walkThroughPages(criteria);
        }
    }

    private void walkThroughSorting(SearchCriteria criteria) {
        for (SearchCriteria.Sorting sort : SearchCriteria.Sorting.values()) {
            criteria.setSrt(sort);
            walkThroughPages(criteria);
        }
    }
}
