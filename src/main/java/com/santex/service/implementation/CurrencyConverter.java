package com.santex.service.implementation;

import com.santex.entity.Currency;
import com.santex.service.CurrencyRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyConverter {
    private static CurrencyRatesService rate;

    @Autowired
    public void setRate(CurrencyRatesService rate) {
        CurrencyConverter.rate = rate;
    }

    static double currencyConverter(int price, Currency currency) {
        int temp = price * rate.obtainRateFromDb(currency);
        if (temp < 10000) return ((double) temp) / 10000;                                      // Less than 1 UAH.
        if (temp >= 10000 && temp <= 100000) return roundToZeroFive((double) temp) / 10000;    // Between 1 UAH and 10 UAH.
        else return roundToZero((double) temp) / 10000;                                        // Greater than 10 UAH.
    }

    private static double roundToZero(double price) {
        return Math.round(price / 10000) * 10000;          // Round to whole UAH.
    }

    private static double roundToZeroFive(double price) {
        return Math.round(price / 500) * 500;              // Round to 0.05 UAH.
    }
}
