package com.santex.controller;

import com.santex.entity.Category;
import com.santex.entity.Subcategory;
import com.santex.service.CategoryService;
import com.santex.service.SubcategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/subcategory")
public class SubcategoryController {
    private final SubcategoryService subcategoryService;
    private final CategoryService categoryService;

    public SubcategoryController(SubcategoryService subcategoryService, CategoryService categoryService) {
        this.subcategoryService = subcategoryService;
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public String getAllSubcategories(Model model) {
        model.addAttribute("subcategories", subcategoryService.findAll());
        return "subcategory-all";
    }

    @PostMapping("/update")
    public String updateSubcategory(@Valid @ModelAttribute("subcategory") Subcategory subcategory,
                                    BindingResult bindingResult,
                                    RedirectAttributes attributes,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            List<Category> categoryList = categoryService.findAll();
            model.addAttribute("category", categoryList);
            return "subcategory-update";
        }
        if (subcategory.getId() == 0) {
            subcategoryService.add(subcategory.getSubcategoryName(), subcategory.getCategory());
            attributes.addFlashAttribute("added", true);
        } else {
            subcategoryService.edit(subcategory.getId(), subcategory.getSubcategoryName(), subcategory.getCategory().getId());
            attributes.addFlashAttribute("edited", true);
        }
        attributes.addFlashAttribute("info", subcategory);
        return "redirect:/admin/subcategory/all";
    }

    @GetMapping("/new")
    public String newSubcategory(Model model) {
        model.addAttribute("category", categoryService.findAll());
        model.addAttribute("subcategory", new Subcategory());
        return "subcategory-update";
    }

    @GetMapping("/edit={id}")
    public String editSubcategory(@PathVariable("id") int id, Model model) {
        model.addAttribute("subcategory", subcategoryService.findById(id));
        model.addAttribute("category", categoryService.findAll());
        return "subcategory-update";
    }

    @GetMapping("/remove={id}")
    public String removeSubcategory(@PathVariable("id") int id, RedirectAttributes attributes) {
        subcategoryService.remove(id);
        attributes.addFlashAttribute("removed", true);
        return "redirect:/admin/subcategory/all";
    }
}
