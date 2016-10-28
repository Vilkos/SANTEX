package com.santex.service;

import com.santex.dto.OrderEntryDto;

import java.util.List;
import java.util.Locale;

public interface PriceService {

    static int toInteger(double price) {
        return ((int) (price * 100));
    }

    static double toDouble(int price) {
        return ((double) price) / 100.0;
    }

    static String toStringWithLocale(double price) {
        return String.format(Locale.forLanguageTag("uk-UA"), price > 10.0 ? "%,.0f" : "%,.2f", price);
    }

    static String toStringEN(double price) {
        return String.format(price > 10.0 ? "%.0f" : "%.2f", price);
    }

    static String toStringWithoutCondition(double price) {
        return String.format(Locale.forLanguageTag("uk-UA"), "%,.2f", price);
    }

    static double total(List<OrderEntryDto> entryList) {
        return entryList.stream().mapToDouble(OrderEntryDto::getSubtotal).sum();
    }

    static double subtotal(OrderEntryDto orderEntryDto) {
        return orderEntryDto.getPriceUAH() * orderEntryDto.getQuantity();
    }
}
