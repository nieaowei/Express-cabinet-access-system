package com.ecas.dao;

import com.ecas.entity.ExpressCabinet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Entity;
import java.util.List;


@RepositoryRestResource
public interface ExpressCainetDAO extends JpaRepository<ExpressCabinet,Long> {
    List<ExpressCabinet> findAllByAddressLike(String address);
}
