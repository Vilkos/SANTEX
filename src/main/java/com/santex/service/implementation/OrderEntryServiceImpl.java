package com.santex.service.implementation;

import com.santex.dao.OrderEntryDao;
import com.santex.dao.ProductDao;
import com.santex.dto.OrderEntryDto;
import com.santex.entity.Order;
import com.santex.entity.OrderEntry;
import com.santex.service.OrderEntryService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.santex.service.PriceService.*;

@Service
@Repository
@Transactional
public class OrderEntryServiceImpl implements OrderEntryService {
    private final OrderEntryDao orderEntryDao;
    private final ProductDao productDao;
    private Function<OrderEntry, OrderEntryDto> orderEntryToOrderEntryDto = oe -> new OrderEntryDto(
            oe.getProduct().getId(),
            oe.obtainOrderId(),
            oe.getProduct().getSKU(),
            oe.getProduct().getProductName(),
            toDouble(oe.getPriceUAH()),
            toStringWithLocale(toDouble(oe.getPriceUAH())),
            oe.getQuantity(),
            oe.getProduct().getUnit().getUnitName(),
            toDouble(oe.subtotal()),
            toStringWithLocale(toDouble(oe.subtotal())));

    public OrderEntryServiceImpl(ProductDao productDao, OrderEntryDao orderEntryDao) {
        this.productDao = productDao;
        this.orderEntryDao = orderEntryDao;
    }

    @Override
    public void add(Order order, int productId, double priceUAH, int quantity) {
        OrderEntry entry = new OrderEntry(
                order,
                productDao.findById(productId),
                toInteger(priceUAH),
                quantity);
        orderEntryDao.save(entry);
    }

    @Override
    public void remove(int orderId) {
        orderEntryDao.deleteAllInBatch(orderEntryDao.getByOrderId(orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderEntryDto> getByOrder(int id) {
        List<OrderEntry> entryList = orderEntryDao.getByOrderId(id);
        return entryList.stream().map(orderEntryToOrderEntryDto).collect(Collectors.toList());
    }
}
