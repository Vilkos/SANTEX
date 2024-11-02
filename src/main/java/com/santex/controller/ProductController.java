package com.santex.controller;

import com.santex.dto.ProductCustomerDto;
import com.santex.dto.SearchCriteria;
import com.santex.dto.ShoppingCart;
import com.santex.entity.Category;
import com.santex.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/products")
@SessionAttributes(types = SearchCriteria.class)
public class ProductController {
    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final SubcategoryService subcategoryService;
    private final ShoppingCart cart;

    public ProductController(ProductService productService, BrandService brandService, CategoryService categoryService, SubcategoryService subcategoryService, ShoppingCart cart) {
        this.productService = productService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.subcategoryService = subcategoryService;
        this.cart = cart;
    }

    @ModelAttribute("criteria")
    public SearchCriteria getSearchCriteria() {
        return new SearchCriteria();
    }

    @ModelAttribute("menu")
    public List<Category> getMainMenu() {
        return categoryService.createMenu();
    }

    @GetMapping("/renew")
    public String root(@ModelAttribute("criteria") SearchCriteria criteria, HttpServletRequest request, HttpSession session) {
        if (request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid()) {
            session.setAttribute("cart", cart);
        }
        criteria.clearCriteria();
        return "redirect:/products";
    }

    @GetMapping
    public String products(@PageableDefault(size = 20) Pageable p,
                           @RequestParam Optional<Integer> catId,
                           @RequestParam Optional<Integer> subId,
                           @RequestParam Optional<Integer> brandId,
                           @RequestParam Optional<String> search,
                           @RequestParam Optional<SearchCriteria.Sorting> srt,
                           @ModelAttribute("criteria") SearchCriteria criteria,
                           Model model) {

        criteria.setCatId(catId.orElse(criteria.getCatId()));
        criteria.setSubId(subId.orElse(criteria.getSubId()));
        criteria.setBrandId(brandId.orElse(criteria.getBrandId()));
        criteria.setSearch(search.orElse(criteria.getSearch()));
        criteria.setSrt(srt.orElse(criteria.getSrt()));

        if (criteria.getCatId() > 0) {
            Optional<Category> category = categoryService.findById(criteria.getCatId());
            category.ifPresent(value -> criteria.setSectionName(value.getCategoryName()));
        }

        if (criteria.getSubId() > 0) {
            criteria.setSectionName(subcategoryService.findById(criteria.getSubId()).getSubcategoryName());
        }

        Page<ProductCustomerDto> page = productService.findByCriteriaCustomer(criteria, p);
        criteria.setBrandList(brandService.findByCriteriaCustomer(criteria));

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 3);
        int end = Math.min(begin + 6, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        return "product-all";
    }
}
