package com.santex.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SearchCriteriaAdmin {
    private String search;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    public SearchCriteriaAdmin() {
        search = "";
        from = LocalDate.of(2016, 1, 1);
        to = LocalDate.now();
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDateTime getStartDateTime() {
        return from.atStartOfDay();
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public LocalDateTime getEndDateTime() {
        return to.atTime(LocalTime.MAX);
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
}
