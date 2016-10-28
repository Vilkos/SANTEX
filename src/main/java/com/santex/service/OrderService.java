package com.santex.service;

import com.santex.dto.SearchCriteriaAdmin;
import com.santex.dto.OrderForm;
import com.santex.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    void add(Order order);

    void edit(OrderForm o);

    void remove(int id);

    Order findById(int id);

    OrderForm getById(int id);

    List<Order> byCustomer(int id);

    List<Order> findAll();

    Page<Order> findByCriteriaAdmin(SearchCriteriaAdmin adminCriteria, Pageable p);
}
