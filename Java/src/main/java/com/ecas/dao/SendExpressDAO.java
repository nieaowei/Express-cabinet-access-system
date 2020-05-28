package com.ecas.dao;

import com.ecas.entity.SendExpress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SendExpressDAO extends JpaRepository<SendExpress,Long> {

}
