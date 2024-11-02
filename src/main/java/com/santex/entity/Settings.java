package com.santex.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.io.Serializable;

import static com.santex.service.PriceService.toDouble;
import static com.santex.service.PriceService.toInteger;

@Entity(name = "settings")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "settings")
public class Settings implements Serializable {
    private static final long serialVersionUID = 3123787455337187761L;
    private int id;
    private int minSum;
    private boolean autoUpdateCurrency;
    private String updateCurrencyUrl;
    private double minSumOfOrder;

    public Settings() {
        id = 1;
        minSumOfOrder = 0;
        autoUpdateCurrency = false;
        updateCurrencyUrl = "";
    }

    @Id
    @Column
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column
    public int getMinSum() {
        return minSum;
    }

    public void setMinSum(int minSum) {
        this.minSum = minSum;
    }

    @Column
    public boolean isAutoUpdateCurrency() {
        return autoUpdateCurrency;
    }

    public void setAutoUpdateCurrency(boolean autoUpdateCurrency) {
        this.autoUpdateCurrency = autoUpdateCurrency;
    }

    @Column
    public String getUpdateCurrencyUrl() {
        return updateCurrencyUrl;
    }

    public void setUpdateCurrencyUrl(String updateCurrencyUrl) {
        this.updateCurrencyUrl = updateCurrencyUrl;
    }

    @Transient
    public double getMinSumOfOrder() {
        return toDouble(minSum);
    }

    public void setMinSumOfOrder(double minSumOfOrder) {
        this.minSumOfOrder = minSumOfOrder;
        this.minSum = toInteger(minSumOfOrder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Settings settings = (Settings) o;

        return id == settings.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
