package com.santex.service;

import com.santex.dto.CurrencyRatesDto;
import com.santex.entity.Currency;

public interface CurrencyRatesService {

    void edit(CurrencyRatesDto currencyRatesDto);

    CurrencyRatesDto obtainRatesForView();

    int obtainRateFromDb(Currency currency);

    void scheduledUpdate();
}
