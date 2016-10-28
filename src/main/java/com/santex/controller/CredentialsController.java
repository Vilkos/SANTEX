package com.santex.controller;

import com.santex.entity.CompanyCredentials;
import com.santex.service.CompanyCredentialsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/credentials")
public class CredentialsController {
    private final CompanyCredentialsService companyCredentialsService;

    public CredentialsController(CompanyCredentialsService companyCredentialsService) {
        this.companyCredentialsService = companyCredentialsService;
    }

    @GetMapping("/edit")
    public String credentialsEdit(Model model) {
        model.addAttribute("credentials", companyCredentialsService.getCredentials());
        return "admin-credentials";
    }

    @PostMapping("/update")
    public String credentialsUpdate(@ModelAttribute("credentials") CompanyCredentials credentials) {
        companyCredentialsService.edit(credentials);
        return "redirect:/admin/credentials/edit";
    }
}
