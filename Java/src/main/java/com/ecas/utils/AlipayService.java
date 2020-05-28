package com.ecas.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;

public final class AlipayService {
    private final static String APP_ID = "2016102200740589";
    private final static String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCSiTXYrdmZ+q75QaxHtNycgE7cIUa752GkToEZRadheIo+QwuBKU0lexhfY0mChAfapzTnLK1c/vpS1vw+bZCHiCcGfFEDkKyLO7397PkoIRlaTpYUBmkhKy9tDE1BWYw20tA96GLafbI3M1fEMTwjDmPmAapYgntM3R0gPT5ulX7IPMZSFdSdeYc9KvalHu1YJdu1ciqVSW4IkvdqG6Qqb1CIeDTwEGAMHygColASvE2+9BOkNbOHdH+uUd5VRxkeBlm1rYXlQIUqmazRycY+rxROQ5KgAXOspyOrF9IWKEswiCfASpgcXyg8PRFbmp8ZIb764TsaxF4GZZXo7UCFAgMBAAECggEAfWN5twq0S8Utmk4u7EEAEc7dLBIxY7KPY7hlUcm/e3FY4gQewQwlXnAhn+Xmzg7KZbh0VWhuhEgOzXn26Dh61u6TbkJMVH+klZOudK0lWshP2G2skEAYD3DTqT1Aki3dBECKPp8uj4XFTOTbMh9rNRgoNXWxXQFyHoiKVr7xQE/RGjWc09oVdhmefcC0G8qh38YsvEr0KORcbkpzA4RItsiUXnIPYG5+0QQBk5jkxUxa2cZMuXbENahvcjD1+tU0iMk4dLCpGvm74Amc9GvnN3qezaqMqHOVlyoCM0PpiLvSaZwCVz3B2vPvmJOJVLEyRrAZq/xU3q/EcLX3uFrTlQKBgQDT3O8cwBHQ2KdgOkvyIZaNJBSLxHoTYcV5afRhrtIlSTuTkZF4887eHn7LOP3eYqBVEkdiEAmnpL3FCHkrWyf/sIu11rw3OAoaG86rb6TSNZEuwb2D7TngAZyJ60TtIfLSxWF6YBkxipGCjdTcT/JMKvWiId59LR0JybaSNlfOtwKBgQCxEEIc4BPQDkjvbRx7NK/6hV6uk3A/icXp4Tkj7AkF8sH5Ll0se5LyH8q5DEL8BkODPCpJtUeY/fWR/BBFw+N5be1z+uAPzf/B+L3q6OdJVv3eD5+75zUXOR6Pxggb/bYoIKhdPtA2LEBj5uqccQVLbjyeOBXxYoKGiVZKu15uowKBgQCnTMflRCbNbMh/TIm8YpmoSt2OoVBlDcgK4IiCurJnjzTkiwb9JEovTJWDlTWj7u39P7nuzgtRvt28QbytaAZl+tvYdAGc+TvA8kR5TGgzNum2qnclPqd9JMiggJClUTGXDxaytEAwxuOw3J9Cej8ztpjRtz0iTAWqPQdW3mLocQKBgAmu2e66UlsObO6rCOOz/eu1X3gsl/bhkfE4X+eXAOdSX0lBP9cu6tpS4sM76G3f6dFDeQyO3VuAh+1z22/1Rntdh57QGXDD0zjIYZp3Ii7gkwGR2SptennKpeE28zqUpWGMZ6ixxrEvulW8SxhDFgcKAEamyoxo1rqEyDNOC9wBAoGAHUh0ZiER/fgLBX1IliEDwMRnEfLTkFIfbfK8bR83GRW4bvWkSECfVA6h0W15/baE0hEIILy81yOYgDkch0BeX5Sb8udBFBiZcCq+3vp6beik0cYT8doJYBRmpvLfYdwZEYK9qYnI6PMjh5vu9DNzlCVR+8giD08rRgARMPWU8gs=";
    private final static String CHARSET = "utf8";
    private final static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkok12K3Zmfqu+UGsR7TcnIBO3CFGu+dhpE6BGUWnYXiKPkMLgSlNJXsYX2NJgoQH2qc05yytXP76Utb8Pm2Qh4gnBnxRA5Csizu9/ez5KCEZWk6WFAZpISsvbQxNQVmMNtLQPehi2n2yNzNXxDE8Iw5j5gGqWIJ7TN0dID0+bpV+yDzGUhXUnXmHPSr2pR7tWCXbtXIqlUluCJL3ahukKm9QiHg08BBgDB8oAqJQErxNvvQTpDWzh3R/rlHeVUcZHgZZta2F5UCFKpms0cnGPq8UTkOSoAFzrKcjqxfSFihLMIgnwEqYHF8oPD0RW5qfGSG++uE7GsReBmWV6O1AhQIDAQAB";
    private final static String SIGN_TYPE = "RSA2";
    private final static String RETURN_URL = "http://118.178.195.221:8080/pay/callback";
    private static final AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);

    public static String createAppPay(String outTradeNo, String amount,String sellerid) {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("我是测试数据");
        model.setSubject("App支付测试Java");
        model.setOutTradeNo(outTradeNo);
        model.setTimeoutExpress("1m");
//        model.setSellerId(sellerid);
        model.setTotalAmount(amount);
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(RETURN_URL);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = AlipayService.alipayClient.sdkExecute(request);
            System.out.println(response);//就是orderString 可以直接给客户端请求，无需再做处理。
            return response.getBody();
//            System.out.println("123");//就是orderString 可以直接给客户端请求，无需再做处理。
//            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
    }
}
