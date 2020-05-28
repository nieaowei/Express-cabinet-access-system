package com.ecas.dao;

import com.ecas.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.validation.annotation.Validated;


@RepositoryRestResource
@Validated
public interface CustomerDAO extends JpaRepository<Customer, Long> {
    Customer findCustomerByPhoneAndPassword(String phone, String password);
    @RestResource()
    Customer getByPhone(String phone);
}
