package com.santex.service.implementation;

import com.santex.dao.SettingsDao;
import com.santex.dto.CurrencyRatesDto;
import com.santex.entity.Settings;
import com.santex.service.SettingsService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Repository
@Transactional
public class SettingsServiceImpl implements SettingsService {
    private final SettingsDao settingsDao;

    public SettingsServiceImpl(SettingsDao settingsDao) {
        this.settingsDao = settingsDao;
    }

    @Override
    public void edit(Settings set) {
        Settings settings = settingsDao.findById(1).orElse(new Settings());
        settings.setMinSumOfOrder(set.getMinSumOfOrder());
        settingsDao.save(settings);
    }

    @Override
    public void edit(CurrencyRatesDto currencyRatesDto) {
        Settings settings = settingsDao.findById(1).orElse(new Settings());
        settings.setAutoUpdateCurrency(currencyRatesDto.isAutoUpdateCurrency());
        settings.setUpdateCurrencyUrl(currencyRatesDto.getUpdateCurrencyUrl());
        settingsDao.save(settings);
    }

    @Override
    @Transactional(readOnly = true)
    public Settings getSettings() {
        return settingsDao.findById(1).orElse(new Settings());
    }
}
