package com.ecas.dao;

import com.ecas.entity.Courier;
import com.ecas.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CourierDAO extends JpaRepository<Courier, Long> {

    Courier findCourierByPhoneAndPassword(String phone, String password);

    Courier getByPhone(String phone);

    List<Courier> findByCompany_Id(long company_id);
}
