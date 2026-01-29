package com.jbeb.pizza.service;

import com.jbeb.pizza.persistence.entity.CustomerEntity;
import com.jbeb.pizza.persistence.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }


    public CustomerEntity findByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }

}
