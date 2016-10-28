package com.santex.controller;

import com.santex.entity.Category;
import com.santex.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public String getAllCategories(Model model) {
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categories", categoryList);
        return "category-all";
    }

    @PostMapping("/update")
    public String createCategory(@Valid @ModelAttribute("category") Category category, BindingResult bindingResult, RedirectAttributes attribute) {
        if (bindingResult.hasErrors()) {
            return "category-update";
        }
        if (category.getId() == 0) {
            categoryService.add(category.getCategoryName());
            attribute.addFlashAttribute("added", true);
        } else {
            categoryService.edit(category.getId(), category.getCategoryName());
            attribute.addFlashAttribute("edited", true);
        }
        attribute.addFlashAttribute("info", category);
        return "redirect:/admin/category/all";
    }

    @GetMapping("/new")
    public String newCategory(Model model) {
        model.addAttribute("category", new Category());
        return "category-update";
    }

    @GetMapping("/edit={id}")
    public String redirectToCategoryEdit(@PathVariable("id") int id, Model model) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "category-update";
    }

    @GetMapping("/remove={id}")
    public String removeCategory(@PathVariable("id") int id, RedirectAttributes attributes) {
        categoryService.remove(id);
        attributes.addFlashAttribute("removed", true);
        return "redirect:/admin/category/all";
    }
}
