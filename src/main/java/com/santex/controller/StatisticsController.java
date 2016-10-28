package com.santex.controller;

import com.santex.dto.StatisticsDto;
import com.santex.service.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/statistic")
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/show")
    public String showStatistic(Model model) {
        model.addAttribute("statistics", statisticsService.getStatistic());
        return "admin-statistics";
    }

    @GetMapping("/update")
    public String updateStatistic(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                  StatisticsDto statisticsDto, Model model) {
        statisticsDto.setFrom(from);
        statisticsDto.setTo(to);
        model.addAttribute("statistics", statisticsService.getStatistic(statisticsDto));
        return "admin-statistics";
    }
}
