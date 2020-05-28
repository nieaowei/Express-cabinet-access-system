package com.ecas.utils;

import com.alibaba.fastjson.JSONObject;
import com.ecas.configs.RabbitMQConfig;
import com.ecas.entity.CourierSendExpress;
import com.ecas.entity.Customer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public final class RabbitMQService {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public static String NotifyMsg(CourierSendExpress courierSendExpress) {
        return "[" + courierSendExpress.getCourier().getCompany().getCompanyName() + "]" +
                "您的快递 " + courierSendExpress.getTrackNo() +
                " 已到达 " + courierSendExpress.getBox().getCabinet().getAddress() +
                " " + courierSendExpress.getBox().getCabinet().getName() + "快递柜 " +
                courierSendExpress.getBox().getName() + "箱,取货码：" +
                courierSendExpress.getCode() + "，时间："
                + courierSendExpress.getSaveTime();
    }

    public static String NotifyMsg(Customer customer) {
        return "";
    }

    public <T, t> void sendExpressNotify(String userID, Result<T,t> msg) {
        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        JSONObject manMap = (JSONObject) JSONObject.toJSON(msg);
        rabbitTemplate.convertAndSend("topicExchange", RabbitMQConfig.TOPIC_NOTIFY_BASE + userID +".express", manMap.toJSONString());
    }

    public <T, t> void sendPayNotify(String userID, Result<T,t> msg) {
        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(msg);
        rabbitTemplate.convertAndSend("topicExchange", RabbitMQConfig.TOPIC_NOTIFY_BASE + userID +".pay", jsonObject.toJSONString());
    }


}
