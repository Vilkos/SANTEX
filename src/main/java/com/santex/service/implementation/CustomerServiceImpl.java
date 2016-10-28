package com.santex.service.implementation;

import com.santex.dao.CustomerDao;
import com.santex.dto.CustomerCreateFormDto;
import com.santex.dto.SearchCriteriaAdmin;
import com.santex.entity.Customer;
import com.santex.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@Repository
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerDao customerDao;
    private final BCryptPasswordEncoder encoder;

    public CustomerServiceImpl(CustomerDao customerDao, BCryptPasswordEncoder encoder) {
        this.customerDao = customerDao;
        this.encoder = encoder;
    }

    @Override
    public void add(String email, String password, String firstName, String secondName, String phone) {
        String encodedPass = encoder.encode(password);
        customerDao.save(new Customer(email, encodedPass, firstName, secondName, phone));
    }

    @Override
    public Customer create(CustomerCreateFormDto form) {
        Customer customer = new Customer();
        customer.setEmail(form.getEmail());
        customer.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
        customer.setFirstName(form.getFirstName());
        customer.setSecondName(form.getSecondName());
        customer.setPhone(form.getPhone());
        customer.setDateOfRegistration(LocalDateTime.now(ZoneId.of("Europe/Kiev")));
        return customerDao.save(customer);
    }

    @Override
    public void edit(CustomerCreateFormDto form) {
        Customer customer = customerDao.findOne(form.getId());
        customer.setEmail(form.getEmail());
        customer.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
        customer.setFirstName(form.getFirstName());
        customer.setSecondName(form.getSecondName());
        customer.setPhone(form.getPhone());
        customerDao.save(customer);
    }

    @Override
    public void remove(int id) {
        customerDao.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findById(int id) {
        return customerDao.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerCreateFormDto findFormById(int id) {
        Customer customer = customerDao.findOne(id);
        CustomerCreateFormDto uf = new CustomerCreateFormDto();
        uf.setId(customer.getId());
        uf.setEmail(customer.getEmail());
        uf.setFirstName(customer.getFirstName());
        uf.setSecondName(customer.getSecondName());
        uf.setPhone(customer.getPhone());
        return uf;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerDao.findByEmailEquals(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> findByCustomerAdmin(SearchCriteriaAdmin customerCriteria, Pageable p) {
        return customerDao.findAll(CustomerSpecifications.byCustomerAdmin(customerCriteria), p);
    }
}
