package com.santex.controller;

import com.santex.dto.CurrencyRatesDto;
import com.santex.service.CurrencyRatesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/currencies")
public class CurrencyController {
    private final CurrencyRatesService currencyRates;

    public CurrencyController(CurrencyRatesService currencyRates) {
        this.currencyRates = currencyRates;
    }

    @GetMapping("/edit")
    public String currencyEdit(Model model) {
        model.addAttribute("currencies", currencyRates.obtainRatesForView());
        return "admin-currencies";
    }

    @PostMapping("/update")
    public String currencyUpdate(@ModelAttribute("currencies") CurrencyRatesDto currencyRatesDto) {
        currencyRates.edit(currencyRatesDto);
        return "redirect:/admin/currencies/edit";
    }
}
