package com.santex.controller;

import com.santex.entity.Settings;
import com.santex.service.SettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/settings")
public class SettingsController {
    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        model.addAttribute("settings", settingsService.getSettings());
        return "admin-settings";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("settings") Settings settings) {
        settingsService.edit(settings);
        return "redirect:/admin/settings/edit";
    }
}
