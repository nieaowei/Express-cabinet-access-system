package com.ecas.services;

import com.ecas.dao.CustomerDAO;
import com.ecas.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    CustomerDAO customerDAO;

    public Customer save(Customer customer){
        return customerDAO.save(customer);
    }

}
