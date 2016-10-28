package com.santex.dto;

public class CurrencyRatesDto {

    private double USDRateFromDb;
    private double EURRateFromDb;
    private double USDRateFromUrl;
    private double EURRateFromUrl;
    private boolean autoUpdateCurrency;
    private String updateCurrencyUrl;


    public CurrencyRatesDto(double USDRateFromDb, double EURRateFromDb, double USDRateFromUrl, double EURRateFromUrl, boolean autoUpdateCurrency, String updateCurrencyUrl) {
        if (USDRateFromDb <= 0) this.USDRateFromDb = 1;
        else this.USDRateFromDb = USDRateFromDb;
        if (EURRateFromDb <= 0) this.EURRateFromDb = 1;
        else this.EURRateFromDb = EURRateFromDb;
        this.USDRateFromUrl = USDRateFromUrl;
        this.EURRateFromUrl = EURRateFromUrl;
        this.autoUpdateCurrency = autoUpdateCurrency;
        this.updateCurrencyUrl = updateCurrencyUrl;
    }

    public CurrencyRatesDto() {
    }

    public double getUSDRateFromUrl() {
        return USDRateFromUrl;
    }

    public void setUSDRateFromUrl(double USDRateFromUrl) {
        this.USDRateFromUrl = USDRateFromUrl;
    }

    public double getEURRateFromUrl() {
        return EURRateFromUrl;
    }

    public void setEURRateFromUrl(double EURRateFromUrl) {
        this.EURRateFromUrl = EURRateFromUrl;
    }

    public double getUSDRateFromDb() {
        return USDRateFromDb;
    }

    public void setUSDRateFromDb(double USDRateFromDb) {
        this.USDRateFromDb = USDRateFromDb;
    }

    public double getEURRateFromDb() {
        return EURRateFromDb;
    }

    public void setEURRateFromDb(double EURRateFromDb) {
        this.EURRateFromDb = EURRateFromDb;
    }

    public boolean isAutoUpdateCurrency() {
        return autoUpdateCurrency;
    }

    public void setAutoUpdateCurrency(boolean autoUpdateCurrency) {
        this.autoUpdateCurrency = autoUpdateCurrency;
    }

    public String getUpdateCurrencyUrl() {
        return updateCurrencyUrl;
    }

    public void setUpdateCurrencyUrl(String updateCurrencyUrl) {
        this.updateCurrencyUrl = updateCurrencyUrl;
    }
}
