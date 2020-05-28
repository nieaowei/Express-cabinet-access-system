package com.ecas.dao;

import com.ecas.entity.ExpressBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface ExpressBoxDAO extends JpaRepository<ExpressBox, Long> {
    ExpressBox findExpressBoxById(Long id);
    List<ExpressBox> findExpressBoxesByCabinet_IdAndIsUsing(Long cabinet_id, int isUsing);

}
