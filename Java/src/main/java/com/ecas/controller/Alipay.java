package com.ecas.controller;

import com.alibaba.fastjson.JSONObject;
import com.ecas.dao.CourierDAO;
import com.ecas.dao.ExpressSendOrderDAO;
import com.ecas.entity.Courier;
import com.ecas.entity.ExpressSendOrder;
import com.ecas.utils.RabbitMQService;
import com.ecas.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
public class Alipay {
    @Resource
    CourierDAO courierDAO;

    @Resource
    ExpressSendOrderDAO expressSendOrderDAO;

    @Autowired
    RabbitMQService rabbitMQService;

    @PostMapping(value = "/pay/callback")
    @Transactional
    public Result<Boolean, List<ObjectError>> callback(HttpServletRequest request) {
        String res = request.getParameter("trade_status");
        String orderNO = request.getParameter("out_trade_no");
        ExpressSendOrder expressSendOrder = expressSendOrderDAO.findByOrderNo(orderNO);
        if (null == expressSendOrder) {
            return new Result<Boolean, List<ObjectError>>().setStatus(400).setMsg("该订单不存在");
        }
        if ("TRADE_CLOSED".equals(res)) {
            //支付成功
            rabbitMQService.sendPayNotify(expressSendOrder.getCustomer().getPhone(), new Result<Boolean, List<ObjectError>>().setStatus(400).setMsg("本次交易已关闭"));
            return new Result<Boolean, List<ObjectError>>().setStatus(400).setMsg("本次交易已关闭");
        }
        if ("TRADE_FINISHED".equals(res)) {
            //支付成功
            rabbitMQService.sendPayNotify(expressSendOrder.getCustomer().getPhone(), new Result<Boolean, List<ObjectError>>().setStatus(400).setMsg("交易已经完成，无需重复支付"));
            return new Result<Boolean, List<ObjectError>>().setStatus(400).setMsg("交易已经完成，无需重复支付");
        }
        if ("WAIT_BUYER_PAY".equals(res)) {
            //支付成功
            rabbitMQService.sendPayNotify(expressSendOrder.getCustomer().getPhone(), new Result<Boolean, List<ObjectError>>().setStatus(400).setMsg("等待交易完成"));
            return new Result<Boolean, List<ObjectError>>().setStatus(400).setMsg("等待交易完成");
        }
        if ("TRADE_SUCCESS".equals(res)) {
            //支付成功
            Random random = new Random();
            int code = random.nextInt(8999) + 1000;
//            String code = Integer.toString(radn);
            expressSendOrder.setStatus(2);
            expressSendOrder.setPaymentTime(new Date());
            expressSendOrder.setCode(code);

            //查找快递员
            List<Courier> couriers = courierDAO.findByCompany_Id(expressSendOrder.getExpress().getCompany().getId());
            if (couriers.size() <= 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                rabbitMQService.sendPayNotify(expressSendOrder.getCustomer().getPhone(), new Result<>().setMsg("支付成功，但该公司暂无合作快递员，稍后为您退款，请注意查收"));

                return new Result<Boolean, List<ObjectError>>().setStatus(400).setMsg("订单分配失败");

            }
            Random random1 = new Random();
            int res1 = random1.nextInt(couriers.size());
            expressSendOrder.setCourier(couriers.get(res1));


            expressSendOrderDAO.save(expressSendOrder);


            Result<ExpressSendOrder, String> msg = new Result<>();
            msg.setStatus(200);
            //        expressSendOrder.setCourier();
            String msg1 = "已分配给快递员 " + couriers.get(res1).getName() + "，电话：" + couriers.get(res1).getPhone() + ",请及时将物品存入快递柜，开柜码：" + code+"（仅有效一次）";
            msg.setMsg(msg1);
            msg.setData(expressSendOrder);

            String msg2 = "您收到新的订单， 请等待用户存件。";

            rabbitMQService.sendPayNotify(expressSendOrder.getCourier().getPhone(), new Result<>().setMsg(msg2));
            rabbitMQService.sendPayNotify(expressSendOrder.getCustomer().getPhone(), msg);
            return new Result<Boolean, List<ObjectError>>().setStatus(200).setMsg("已成功分配快递员，支付成功");

        }
        return new Result<Boolean, List<ObjectError>>().setStatus(400).setMsg("支付失败");
    }
}
