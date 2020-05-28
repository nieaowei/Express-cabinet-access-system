package com.ecas.dao;

import com.ecas.entity.ExpressSendOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource
public interface ExpressSendOrderDAO extends JpaRepository<ExpressSendOrder,Long> {
    List<ExpressSendOrder> findByCourier_IdAndStatus(Long courier_id, int status);
    ExpressSendOrder findByCustomer_IdAndOrderNo(Long customer_id, String orderNo);
    List<ExpressSendOrder> findByCustomer_Id(Long customer_id);
    List<ExpressSendOrder> findByCreateTimeBeforeAndStatus(Date createTime, int status);
    ExpressSendOrder findByOrderNo(String orderNo);
    ExpressSendOrder findByExpress_Box_IdAndCodeAndIsSave(Long express_box_id, int code, int isSave);
}
