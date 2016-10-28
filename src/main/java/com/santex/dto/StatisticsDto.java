package com.santex.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class StatisticsDto {
    private double sum;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    public StatisticsDto(double sum, LocalDate from, LocalDate to) {
        this.sum = sum;
        this.from = from;
        this.to = to;
    }

    public StatisticsDto() {
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
}
