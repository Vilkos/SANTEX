package com.santex.dao;

import com.santex.entity.OrderEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderEntryDao extends JpaRepository<OrderEntry, Integer> {

    @Query("select oe from order_entry oe where oe.orders.id = :id")
    List<OrderEntry> getByOrderId(@Param("id") int id);

    @Query(value = "select sum(quantity * priceUAH) from order_entry inner join orders on order_id = id where timeOfOrder between :startDate and :endDate",nativeQuery = true)
    Integer getSumByDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "select sum(quantity * priceUAH) from order_entry ", nativeQuery = true)
    Integer getWholeSum();

    @Query(value = "select min(timeOfOrder) FROM orders", nativeQuery = true)
    LocalDateTime getMinDate();
}
