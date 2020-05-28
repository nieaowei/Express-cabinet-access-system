package com.ecas.dao;

import com.ecas.entity.Courier;
import com.ecas.entity.CourierRentBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource
public interface CourierRentBoxDAO extends JpaRepository<CourierRentBox,Long> {
    CourierRentBox findByBox_IdAndFinishFlag(Long box_id, int finishFlag);
    List<CourierRentBox> findByBox_IdAndCourier_IdAndFinishFlag(Long box_id, Long courier_id, int finishFlag);

    List<CourierRentBox> findByBox_IdAndCourier_Id(Long box_id, Long courier_id);

    List<CourierRentBox> findByCourier_IdAndFinishFlag(Long courier_id, int finishFlag);

    List<CourierRentBox> findByFinishTimeBeforeAndFinishFlag(Date finishTime, int finishFlag);


}
