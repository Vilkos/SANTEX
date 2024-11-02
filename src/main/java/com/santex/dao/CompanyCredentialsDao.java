package com.santex.dao;

import com.santex.entity.CompanyCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import jakarta.persistence.QueryHint;
import java.util.Optional;

public interface CompanyCredentialsDao extends JpaRepository<CompanyCredentials, Integer> {

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Optional<CompanyCredentials> findById(int id);
}
