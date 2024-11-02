package com.santex.dao;

import com.santex.entity.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;

import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface CategoryDao extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Optional<Category> findByCategoryName(String name);

    @Override
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<Category> findAll(Specification specification);

    @Override
    @QueryHints({
            @QueryHint(name = "org.hibernate.cacheable", value = "true"),
            @QueryHint(name = "jakarta.persistence.cache.storeMode", value = "REFRESH")})
    List<Category> findAll();
}
