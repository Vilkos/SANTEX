package com.santex.dao;

import com.santex.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDao extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {

    List<Order> findByCustomer_IdEquals(@Param("id") int id);

    @Override
    @EntityGraph(attributePaths = "customer", type = EntityGraph.EntityGraphType.FETCH)
    Page<Order> findAll(Specification specification, Pageable pageable);

    @EntityGraph(attributePaths = "customer", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select o from orders o where o.id = :id")
    Order findById(@Param("id") int id);
}
