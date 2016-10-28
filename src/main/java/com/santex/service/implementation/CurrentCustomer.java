package com.santex.service.implementation;

import com.santex.entity.Customer;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentCustomer extends org.springframework.security.core.userdetails.User {

    private Customer customer;

    CurrentCustomer(Customer customer) {
        super(customer.getEmail(), customer.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getId() {
        return customer.getId();
    }
}
