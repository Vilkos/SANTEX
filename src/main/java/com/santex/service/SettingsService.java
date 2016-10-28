package com.santex.service;

import com.santex.dto.CurrencyRatesDto;
import com.santex.entity.Settings;

public interface SettingsService {

    void edit(Settings settings);

    void edit(CurrencyRatesDto currencyRatesDto);

    Settings getSettings();
}
