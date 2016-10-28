package com.santex.service;

import com.santex.dto.OrderEntryDto;
import com.santex.entity.Order;
import com.santex.entity.OrderEntry;

import java.util.List;

public interface OrderEntryService {

    void add(Order order, int productId, double priceUAH, int quantity);

    void remove(int orderId);

    List<OrderEntryDto> getByOrder(int id);
}
