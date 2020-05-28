package com.ecas.dao;

import com.ecas.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface CompanyDAO extends JpaRepository<Company, Long> {

}
