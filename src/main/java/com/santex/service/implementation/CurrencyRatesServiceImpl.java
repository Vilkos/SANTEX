package com.santex.service.implementation;

import com.santex.dao.CurrencyRatesDao;
import com.santex.dto.CurrencyRatesDto;
import com.santex.entity.Currency;
import com.santex.entity.CurrencyRate;
import com.santex.dto.Event;
import com.santex.service.CurrencyRatesService;
import com.santex.service.PriceService;
import com.santex.service.SettingsService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@Service
@Repository
public class CurrencyRatesServiceImpl implements CurrencyRatesService {
    private final CurrencyRatesDao currencyRatesDao;
    private final SettingsService settingsService;
    private final ApplicationEventPublisher publisher;

    public CurrencyRatesServiceImpl(CurrencyRatesDao currencyRatesDao, SettingsService settingsService, ApplicationEventPublisher publisher) {
        this.currencyRatesDao = currencyRatesDao;
        this.settingsService = settingsService;
        this.publisher = publisher;
    }

    @Override
    public void edit(CurrencyRatesDto currencyRatesDto) {
        if (currencyRatesDto.isAutoUpdateCurrency()) {
            saveRateToDb(Currency.USD, PriceService.toInteger(currencyRatesDto.getUSDRateFromUrl()));
            saveRateToDb(Currency.EUR, PriceService.toInteger(currencyRatesDto.getEURRateFromUrl()));
        } else {
            saveRateToDb(Currency.USD, PriceService.toInteger(currencyRatesDto.getUSDRateFromDb()));
            saveRateToDb(Currency.EUR, PriceService.toInteger(currencyRatesDto.getEURRateFromDb()));
        }
        settingsService.edit(currencyRatesDto);
    }

    @Transactional
    public void saveRateToDb(Currency currency, int rate) {
        CurrencyRate rateForSave = currencyRatesDao.findByCurrency(currency).orElse(new CurrencyRate(currency, 100));
        if (rate != rateForSave.getRate()) {
            rateForSave.setRate(rate);
            currencyRatesDao.save(rateForSave);
            publisher.publishEvent(new Event());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CurrencyRatesDto obtainRatesForView() {
        Map<Currency, Integer> ratesFromUrl = obtainRatesFromUrl();
        int UsdRateFromDb = obtainRateFromDb(Currency.USD);
        int EurRateFromDb = obtainRateFromDb(Currency.EUR);
        return new CurrencyRatesDto(
                PriceService.toDouble(UsdRateFromDb),
                PriceService.toDouble(EurRateFromDb),
                PriceService.toDouble(ratesFromUrl.getOrDefault(Currency.USD, 0)),
                PriceService.toDouble(ratesFromUrl.getOrDefault(Currency.EUR, 0)),
                settingsService.getSettings().isAutoUpdateCurrency(),
                settingsService.getSettings().getUpdateCurrencyUrl());
    }

    @Override
    @Transactional(readOnly = true)
    public int obtainRateFromDb(Currency currency) {
        if (currency == Currency.UAH) {
            return 100;
        } else {
            Optional<CurrencyRate> rate = currencyRatesDao.findByCurrency(currency);
            if (rate.isPresent()) {
                return rate.get().getRate();
            } else return 100;
        }
    }

    private Map<Currency, Integer> obtainRatesFromUrl() {
        Map<Currency, Integer> ratesFromUrl = new HashMap<>();
        String updateCurrencyUrl = settingsService.getSettings().getUpdateCurrencyUrl();
        if (!updateCurrencyUrl.isEmpty()) {
            try {
                URL url = new URL(updateCurrencyUrl);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", USER_AGENT);
                InputStream is = connection.getInputStream();
                JsonReader reader = Json.createReader(is);
                JsonArray array = reader.readArray();
                reader.close();
                is.close();
                for (JsonObject object : array.getValuesAs(JsonObject.class)) {
                    if (object.getJsonString("ccy").getString().equals("USD")) {
                        Integer rate = PriceService.toInteger(Double.valueOf(object.getJsonString("sale").getString()));
                        if (rate != 0) ratesFromUrl.put(Currency.USD, rate);
                    }
                    if (object.getJsonString("ccy").getString().equals("EUR")) {
                        Integer rate = PriceService.toInteger(Double.valueOf(object.getJsonString("sale").getString()));
                        if (rate != 0) ratesFromUrl.put(Currency.EUR, rate);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ratesFromUrl;
    }

    @Override
    @Scheduled(cron = "0 0 10,13,16 * * MON-FRI", zone = "Europe/Kiev")
    public void scheduledUpdate() {
        if (settingsService.getSettings().isAutoUpdateCurrency()) {
            Map<Currency, Integer> ratesMap = obtainRatesFromUrl();
            if (!ratesMap.isEmpty()) {
                saveRateToDb(Currency.USD, ratesMap.get(Currency.USD));
                saveRateToDb(Currency.EUR, ratesMap.get(Currency.EUR));
            }
        }
    }
}