package com.santex.controller;

import com.santex.entity.Brand;
import com.santex.entity.CompanyCredentials;
import com.santex.service.BrandService;
import com.santex.service.CompanyCredentialsService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ApplicationControllerAdvice {
    private final BrandService brandService;
    private final CompanyCredentialsService companyCredentials;

    public ApplicationControllerAdvice(BrandService brandService, CompanyCredentialsService companyCredentials) {
        this.brandService = brandService;
        this.companyCredentials = companyCredentials;
    }

    @ModelAttribute("credentials")
    public CompanyCredentials getCredentials() {
        return companyCredentials.getCredentials();
    }

    @ModelAttribute("brandsForView")
    public List<Brand> getBrandLogos() {
        return brandService.findForFooterLogos();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException(Model model) {
        model.addAttribute("message", "Неможливо видалити об'єкт з бази, оскільки він вже використовується.");
        return "admin-error";
    }
}
