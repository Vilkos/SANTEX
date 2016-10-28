package com.santex.dao;

import com.santex.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface ProductDao extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    @Query("select s from product s where s.SKU like :SKU")
    Optional<Product> findBySKU(@Param("SKU") String SKU);

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("select p from product p where p.availability = true order by p.productName asc")
    List<Product> findAllForCustomerPrice();

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Override
    Page<Product> findAll(Specification specification, Pageable pageable);

    @Query("select p from product p where p.id = :id")
    Product findById(@Param("id") int id);
}
