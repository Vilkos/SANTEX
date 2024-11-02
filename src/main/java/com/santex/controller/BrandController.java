package com.santex.controller;

import com.santex.entity.Brand;
import com.santex.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/brand")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/all")
    public String getAllBrands(Model model) {
        List<Brand> brandList = brandService.findAll();
        model.addAttribute("brands", brandList);
        return "brand-all";
    }

    @PostMapping("/update")
    public String createBrand(@Valid @ModelAttribute("brand") Brand brand, BindingResult bindingResult, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            System.out.println("errors are present");
            return "brand-update";
        }
        if (brand.getId() == 0) {
            brandService.add(
                    brand.getBrandName(),
                    brand.getLogo(),
                    brand.getUrl());
            attributes.addFlashAttribute("added", true);
        } else {
            brandService.edit(
                    brand.getId(),
                    brand.getBrandName(),
                    brand.isLogoAvailability(),
                    brand.getLogo(),
                    brand.getUrl());
            attributes.addFlashAttribute("edited", true);
        }
        attributes.addFlashAttribute("info", brand);
        return "redirect:/admin/brand/all";
    }

    @GetMapping("/new")
    public String newBrand(Model model) {
        model.addAttribute("brand", new Brand());
        return "brand-update";
    }

    @GetMapping("/edit={id}")
    public String redirectToBrandEdit(@PathVariable("id") int id, Model model) {
        Brand brand = brandService.findById(id);
        model.addAttribute("brand", brand);
        return "brand-update";
    }

    @GetMapping("/remove={id}")
    public String removeBrand(@PathVariable("id") int id, RedirectAttributes attributes) {
        brandService.remove(id);
        attributes.addFlashAttribute("removed", true);
        return "redirect:/admin/brand/all";
    }
}
