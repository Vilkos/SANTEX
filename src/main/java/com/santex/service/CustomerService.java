package com.santex.service;

import com.santex.dto.CustomerCreateFormDto;
import com.santex.dto.SearchCriteriaAdmin;
import com.santex.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomerService {

    void add(String email, String password, String firstName, String secondName, String phone);

    Customer create(CustomerCreateFormDto form);

    void edit(CustomerCreateFormDto form);

    void remove( int id);

    Optional<Customer> findById(int id);

    CustomerCreateFormDto findFormById(int id);

    Optional<Customer> getCustomerByEmail(String email);

    Page<Customer> findByCustomerAdmin(SearchCriteriaAdmin customerCriteria, Pageable p);
}
