package com.santex.dao;

import com.santex.entity.Subcategory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.QueryHint;
import java.util.List;

public interface SubcategoryDao extends JpaRepository<Subcategory, Integer>, JpaSpecificationExecutor<Subcategory> {

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("select s from subcategory s where s.category.id = :id")
    List<Subcategory> findByCategoryId(@Param("id") int id);

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("select s from subcategory s where s.category.id = :catId and s.subcategoryName =:name")
    Subcategory getByCatIdAndSubName(@Param("catId") int catId, @Param("name") String subcategoryName);

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Override
    List<Subcategory> findAll(Specification specification);

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Override
    List<Subcategory> findAll();
}
