package com.santex.service.implementation;

import com.santex.dto.SearchCriteriaAdmin;
import com.santex.dao.OrderDao;
import com.santex.dao.OrderEntryDao;
import com.santex.dao.CustomerDao;
import com.santex.dto.OrderForm;
import com.santex.dto.OrderEntryDto;
import com.santex.entity.Customer;
import com.santex.entity.Order;
import com.santex.entity.OrderEntry;
import com.santex.service.OrderEntryService;
import com.santex.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.santex.service.PriceService.total;
import static com.santex.service.implementation.OrderSpecifications.byOrderAdmin;

@Service
@Repository
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final CustomerDao customerDao;
    private final OrderEntryDao orderEntryDao;
    private final OrderEntryService orderEntryService;

    public OrderServiceImpl(OrderEntryService orderEntryService, OrderDao orderDao, CustomerDao customerDao, OrderEntryDao orderEntryDao) {
        this.orderEntryService = orderEntryService;
        this.orderDao = orderDao;
        this.customerDao = customerDao;
        this.orderEntryDao = orderEntryDao;
    }

    @Override
    public void add(Order order) {
        Customer customer = customerDao.findOne(order.getCustomer().getId());
        order.setCustomer(customer);
        orderDao.save(order);
    }

    @Override
    public void edit(OrderForm o) {
        Order order = orderDao.findOne(o.getId());
        order.setStreet(o.getStreet());
        order.setCity(o.getCity());
        order.setPostcode(o.getPostcode());
        order.setStatus(o.getStatus());
        orderDao.save(order);
        List<OrderEntryDto> entriesFromForm = o.getEntryList();

        List<OrderEntry> entriesFromDb = orderEntryDao.getByOrderId(o.getId());

        for (OrderEntry entry : entriesFromDb) {
            int id = entry.obtainOrderId();
            if (!entriesFromForm.stream().filter(e -> e.getOrderId() == id).findFirst().isPresent()) {
                orderEntryDao.delete(entry);
            }
        }

        for (OrderEntryDto entry : entriesFromForm) {
            orderEntryService.add(orderDao.findOne(o.getId()), entry.getId(), entry.getPriceUAH(), entry.getQuantity());
        }
    }

    @Override
    public void remove(int id) {
        orderEntryDao.deleteInBatch(orderEntryDao.getByOrderId(id));
        orderDao.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Order findById(int id) {
        return orderDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderForm getById(int id) {
        Order o = orderDao.findById(id);
        List<OrderEntryDto> entryList = orderEntryService.getByOrder(id);
        return new OrderForm(
                o.getId(),
                o.getStreet(),
                o.getCity(),
                o.getPostcode(),
                o.getMessage(),
                o.getStatus(),
                o.showTimeOfOrder(),
                o.getCustomer(),
                orderEntryService.getByOrder(id),
                total(entryList));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> byCustomer(int id) {
        return orderDao.findByCustomer_IdEquals(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findByCriteriaAdmin(SearchCriteriaAdmin adminCriteria, Pageable p) {
        return orderDao.findAll(byOrderAdmin(adminCriteria), p);
    }
}
