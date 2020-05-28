package com.ecas.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import org.junit.Test;

import static org.junit.Assert.*;

public class AlipayServiceTest {


    @Test
    public void testAlipay(){
//        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
////SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
//        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
////        model.setBody("我是测试数据");
////        model.setSubject("App支付测试Java");
//        model.setOutTradeNo("123456");
////        model.setTimeoutExpress("30m");
//        model.setTotalAmount("0.01");
////        model.setProductCode("QUICK_MSECURITY_PAY");
//        request.setBizModel(model);
////        request.setNotifyUrl("商户外网可以访问的异步地址");
//        try {
//            //这里和普通的接口调用不同，使用的是sdkExecute
//            AlipayTradeAppPayResponse response = AlipayService.alipayClient.sdkExecute(request);
//            System.out.println(response);//就是orderString 可以直接给客户端请求，无需再做处理。
//            System.out.println("123");//就是orderString 可以直接给客户端请求，无需再做处理。
//            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
    }
}