package com.santex.dao;

import com.santex.entity.Currency;
import com.santex.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.Optional;

public interface CurrencyRatesDao extends JpaRepository<CurrencyRate, Integer> {

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Optional<CurrencyRate> findByCurrency(Currency currency);
}
