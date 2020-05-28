package com.ecas.dao;

import com.ecas.entity.Courier;
import com.ecas.entity.CourierSendExpress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CourierSendExpressDAO extends JpaRepository<CourierSendExpress,Long> {
    List<CourierSendExpress> findAllByCourier_Id(Long courier_id);
    List<CourierSendExpress> findByReciverPhone(String reciverPhone);
    CourierSendExpress findByBox_IdAndCodeAndIsPick(Long box_id, Long code, int isPick);
}
