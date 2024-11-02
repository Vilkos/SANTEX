package com.santex.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.io.Serializable;

@Entity(name = "currency_rates")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "currency")
public class CurrencyRate implements Serializable {
    private static final long serialVersionUID = 2994457356236465628L;
    private Currency currency;
    private int rate;

    public CurrencyRate(Currency currency, int rate) {
        this.currency = currency;
        this.rate = rate;
    }

    public CurrencyRate() {
        rate = 100;
    }

    @Id
    @Column(length = 3)
    @Enumerated(EnumType.STRING)
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Column(nullable = false)
    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyRate that = (CurrencyRate) o;

        return currency == that.currency;

    }

    @Override
    public int hashCode() {
        return currency.hashCode();
    }
}
