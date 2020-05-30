package com.ecas.tasks;

import com.ecas.dao.CourierRentBoxDAO;
import com.ecas.dao.ExpressBoxDAO;
import com.ecas.dao.ExpressSendOrderDAO;
import com.ecas.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync
public class RentBoxCleanTask {
    @Autowired
    ExpressBoxDAO expressBoxDAO;

    @Autowired
    CourierRentBoxDAO courierRentBoxDAO;

    @Autowired
    ExpressSendOrderDAO expressSendOrderDAO;

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void task1(){
        /**
         * 清除过期订单
         */
        Date date = new Date();
        List<ExpressSendOrder> expressSendOrders  = expressSendOrderDAO.findByCreateTimeBeforeAndStatus(date,1);
        for (ExpressSendOrder sendOrder:expressSendOrders) {
            sendOrder.setStatus(0);
        }
        expressSendOrderDAO.saveAll(expressSendOrders);
    }

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void task2(){
        /**
         * 清除过期租用盒子
         */
        Date date = new Date();
        List<CourierRentBox> courierRentBoxes  = courierRentBoxDAO.findByFinishTimeBeforeAndFinishFlag(date,0);
        for (CourierRentBox courierRentBox:courierRentBoxes) {
            courierRentBox.setFinishFlag(1);
            courierRentBox.getBox().setIsUsing(0);
//            courierRentBox.getExpressBox().setIsUsing(0);
            expressBoxDAO.save(courierRentBox.getBox());
        }
        courierRentBoxDAO.saveAll(courierRentBoxes);
    }
}
