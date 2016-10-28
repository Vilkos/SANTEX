package com.santex.controller;

import com.santex.entity.Unit;
import com.santex.service.UnitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/unit")
public class UnitController {
    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping("/all")
    public String getAllUnits(Model model) {
        List<Unit> unitList = unitService.findAll();
        model.addAttribute("units", unitList);
        return "unit-all";
    }

    @PostMapping("/update")
    public String createUnit(@Valid @ModelAttribute("unit") Unit unit, BindingResult bindingResult, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            return "unit-update";
        }
        if (unit.getId() == 0) {
            unitService.add(unit.getUnitName());
            attributes.addFlashAttribute("added", true);
        } else {
            unitService.edit(unit.getId(), unit.getUnitName());
            attributes.addFlashAttribute("edited", true);
        }
        attributes.addFlashAttribute("info", unit);
        return "redirect:/admin/unit/all";
    }

    @GetMapping("/new")
    public String newUnit(Model model) {
        model.addAttribute("unit", new Unit());
        return "unit-update";
    }

    @GetMapping("/edit={id}")
    public String redirectUnitToEdit(@PathVariable("id") int id, Model model) {
        Unit unit = unitService.findById(id);
        model.addAttribute("unit", unit);
        return "unit-update";
    }

    @GetMapping("/remove={id}")
    public String removeUnit(@PathVariable("id") int id, RedirectAttributes attributes) {
        unitService.remove(id);
        attributes.addFlashAttribute("removed", true);
        return "redirect:/admin/unit/all";
    }
}
