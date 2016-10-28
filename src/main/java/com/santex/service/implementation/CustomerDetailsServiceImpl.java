package com.santex.service.implementation;

import com.santex.dao.CustomerDao;
import com.santex.entity.Customer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class CustomerDetailsServiceImpl implements UserDetailsService {
    private final CustomerDao customerDao;

    public CustomerDetailsServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    @Transactional(readOnly = true)
    public CurrentCustomer loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerDao.findByEmailEquals(email).orElseThrow(() -> new UsernameNotFoundException(String.format("Customer with email=%s was not found", email)));

        return new CurrentCustomer(customer);
    }
}
