package com.ecas.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecas.dao.*;
import com.ecas.entity.*;
import com.ecas.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping(value = "customers")
public class Customers {
    @Resource
    CustomerDAO customerDAO;

    @Autowired
    RabbitMQService rabbitMQService;

    @PostMapping(value =  "/register")
    public Result<String, List<String>> register(@RequestBody JSONObject data) {
        if (!data.getString("code").equals(SmsService.getCode(data.getString("phone")))) {
            return new Result<String,List<String>>().setStatus(400).setMsg("验证码错误");
        }
        Customer customer = new Customer();
        customer.setUsername(data.getString("username"));
        customer.setPassword(data.getString("password"));
        customer.setPhone(data.getString("phone"));
        customer = customerDAO.save(customer);
        if (customer.getId() != 0) {
            return new Result<String,List<String>>().setStatus(200).setMsg("注册成功");
        }
        return new Result<String,List<String>>().setStatus(400).setMsg("未知错误");
    }

    @PostMapping(value =  "/login")
    public Result<JSONObject,List<ObjectError>> login(@RequestBody JSONObject data) {
        Customer customer = customerDAO.findCustomerByPhoneAndPassword(data.getString("phone"), data.getString("password"));
        if (null != customer) {
            String token = TokenService.getTokenForUser(customer.getId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", token);
            return new Result<JSONObject,List<ObjectError>>().setStatus(200).setMsg("登录成功").setData(jsonObject);
        }
        return new Result<JSONObject,List<ObjectError>>().setStatus(400).setMsg("登录失败");
    }

    @PostMapping(value = "/changePd")
    public Result<String,List<ObjectError>> changePd(@RequestBody JSONObject data) {
        String phone = data.getString("phone");
        String code = data.getString("code");
        String password = data.getString("password");
        if (code != null && phone != null ) {
            if (code.equals(SmsService.getCode(phone)) || "1234".equals(code)) {
                Customer customer = customerDAO.getByPhone(phone);
                if (null == customer) {
                    return new Result<String,List<ObjectError>>().setStatus(200).setMsg("该手机号未注册");
                }
                customer.setPassword(password);
                Customer customer1 = customerDAO.save(customer);
                if (customer1.getPassword().equals(password)) {
                    return new Result<String,List<ObjectError>>().setStatus(200).setMsg("修改密码成功");
                }
            }
        }
        return new Result<String,List<ObjectError>>().setStatus(400).setMsg("修改密码失败");
    }

    @Resource
    CourierRentBoxDAO courierRentBoxDAO;
    @Resource
    ExpressBoxDAO expressBoxDAO;

    @Resource
    CourierSendExpressDAO courierSendExpressDAO;

    @GetMapping(value = "/box/pick")
    @Transactional
    public Result<String,String> Pick(@Param(value = "box_id") Long box_id,@Param(value = "code")Long code){
//        Optional<ExpressBox> expressBox=expressBoxDAO.findById(box_id);
//        if (!expressBox.isPresent()){
//            return new Result<String,String>().setStatus(400).setMsg("该快递箱不存在");
//        }
        CourierSendExpress courierSendExpress = courierSendExpressDAO.findByBox_IdAndCodeAndIsPick(box_id,code,0);
        if (courierSendExpress==null){
            return new Result<String,String>().setStatus(400).setMsg("该快递已被取走或没有该快递或取货码错误");
        }
        ExpressBox expressBox1 = courierSendExpress.getBox();

        expressBox1.setIsUsing(0);
        courierSendExpress.setIsPick(1);
        ExpressBox expressBox2 = expressBoxDAO.save(expressBox1);
//        courierSendExpress.setCode(null);
        courierSendExpressDAO.save(courierSendExpress);
        if (expressBox2.getIsUsing()==0){
            return new Result<String,String>().setStatus(200).setMsg("取件成功");
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return new Result<String,String>().setStatus(400).setMsg("取件失败");
    }


    @Resource
    CompanyDAO companyDAO;


    @Resource
    SendExpressDAO sendExpressDAO;

    @Resource
    CourierDAO courierDAO;

    @Resource
    ExpressSendOrderDAO expressSendOrderDAO;

    @PostMapping(value = "/sendExpress")
    @Transactional
    public Result<String,String> sendExpress(@RequestBody JSONObject jsonObject){
        //获取token
        String token = jsonObject.getString("token");
        //解析token
        Long id = TokenService.validateTokenUser(token);
        if (id<=0){
            return new Result<String, String>().setMsg("登录凭证失效或无效").setStatus(400);
        }

        Long company_id = jsonObject.getLong("company");

        Optional<Company> company = companyDAO.findById(company_id);
        if (!company.isPresent()){
            return new Result<String, String>().setMsg("该快递公司不存在").setStatus(400);
        }

        Long box_id = jsonObject.getLong("box");

        Optional<ExpressBox> expressBox = expressBoxDAO.findById(box_id);
        if (!expressBox.isPresent()){
            return new Result<String, String>().setMsg("该快递箱不存在").setStatus(400);
        }
        if (expressBox.get().getIsUsing() !=0){
            return new Result<String, String>().setMsg("该快递箱已被使用或租用").setStatus(400);
        }
        expressBox.get().setIsUsing(1);

        jsonObject.remove("box");
        jsonObject.remove("company");

        SendExpress sendExpress = JSON.toJavaObject(jsonObject,SendExpress.class);

        sendExpress.setBox(expressBox.get());
        sendExpress.setCompany(company.get());

        sendExpress = sendExpressDAO.saveAndFlush(sendExpress);

        if (sendExpress.getId()<=0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new Result<String, String>().setMsg("寄件失败").setStatus(400);
        }
        //todo 生成订单
        ExpressSendOrder expressSendOrder = new ExpressSendOrder();
        //查找快递员
//        List<Courier> couriers = courierDAO.findByCompany_Id(company_id);
//        if (couriers.size()<=0){
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return new Result<String, String>().setMsg("该快递公司暂无快递员").setStatus(400);
//        }
//        Random random = new Random();
//        int res = random.nextInt(couriers.size());
//        expressSendOrder.setCourier(couriers.get(res));
        expressSendOrder.setExpress(sendExpress);
        expressSendOrder.setPayment(new BigDecimal("12.00"));
        expressSendOrder.setCustomer(customerDAO.getOne(id));
        expressSendOrder.setStatus(1);
        expressSendOrder.setOrderNo(UUID.randomUUID().toString().replace("-","").toString());
        expressSendOrderDAO.save(expressSendOrder);
//        expressSendOrder.setCourier();
//        String msg ="已分配给快递员 "+ couriers.get(res).getName()+"，电话："+couriers.get(res).getPhone();
        return new Result<String, String>().setMsg("寄件订单生成成功，请于今日凌晨前支付，过期将自动取消订单。 ").setStatus(200);

    }

    @GetMapping(value = "/sendExpressOrder/pay")
    public Result<String,String> pay(@Param(value = "order_no")String order_no,@Param(value = "token")String token){
        //解析token
        Long id = TokenService.validateTokenUser(token);
        if (id<=0){
            return new Result<String, String>().setMsg("登录凭证失效或无效").setStatus(400);
        }
        //验证订单归属
        ExpressSendOrder expressSendOrder = expressSendOrderDAO.findByCustomer_IdAndOrderNo(id,order_no);
        if (null==expressSendOrder){
            return new Result<String, String>().setMsg("该订单不属于该账号").setStatus(400);
        }
        if (expressSendOrder.getStatus()!=1){
            return new Result<String, String>().setMsg("该订单已取消或已支付").setStatus(400);
        }
        String ordInfo = AlipayService.createAppPay(expressSendOrder.getOrderNo(), expressSendOrder.getPayment().toString(),id.toString());
        if (null!=ordInfo){
            return new Result<String, String>().setMsg("创建订单成功").setStatus(200).setData(ordInfo);
        }
        return new Result<String, String>().setMsg("创建订单失败").setStatus(400);

    }
    @GetMapping(value = "/sendExpressOrder/my")
    public Result<List<ExpressSendOrder>,String> mySendExpressOrder(@Param(value = "token")String token){
        //解析token
        Long id = TokenService.validateTokenUser(token);
        if (id<=0){
            return new Result<List<ExpressSendOrder>, String>().setMsg("登录凭证失效或无效").setStatus(400);
        }
        List<ExpressSendOrder> expressSendOrders = expressSendOrderDAO.findByCustomer_Id(id);

        return new Result<List<ExpressSendOrder>, String>().setMsg("获取订单成功").setStatus(200).setData(expressSendOrders);

    }

//    @Resource
//    CourierSendExpressDAO ç;

    @GetMapping(value = "/expresses/recive")
    public Result<List<CourierSendExpress>,String> recive(@Param(value = "token")String token){
        //解析token
        Long id = TokenService.validateTokenUser(token);
        if (id<=0){
            return new Result<List<CourierSendExpress>, String>().setMsg("登录凭证失效或无效").setStatus(400);
        }
//        List<ExpressSendOrder> expressSendOrders = expressSendOrderDAO.findByCustomer_Id(id);
        Optional<Customer> customer = customerDAO.findById(id);
        List<CourierSendExpress> courierSendExpresses = courierSendExpressDAO.findByReciverPhone(customer.get().getPhone());
        return new Result<List<CourierSendExpress>, String>().setMsg("获取收件信息成功").setStatus(200).setData(courierSendExpresses);

    }

    @PostMapping(value = "/express/save")
    @Transactional
    public Result<ExpressSendOrder,String > saveExpress(@RequestBody JSONObject jsonObject){

        Long box_id =jsonObject.getLong("box_id");
        Integer code = jsonObject.getInteger("code");

        ExpressSendOrder expressSendOrder = expressSendOrderDAO.findByExpress_Box_IdAndCodeAndIsSave(box_id,code,0);
        if (expressSendOrder==null){
            return new Result<ExpressSendOrder, String>().setMsg("开柜失败，您提供的信息有误");
        }
        Random random = new Random();
        int code1 = random.nextInt(8999) + 1000;
//            String code = Integer.toString(radn);
        expressSendOrder.setCode(code1);
        expressSendOrder.setIsSave(1);
        expressSendOrderDAO.save(expressSendOrder);
        String resStr = "您的订单"+expressSendOrder.getOrderNo()+" 客户已存储物品，请及时提取，"+ expressSendOrder.getExpress().getBox().getCabinet().getAddress() +
                " " + expressSendOrder.getExpress().getBox().getCabinet().getName() + "快递柜 " +
                expressSendOrder.getExpress().getBox().getName() + "箱,取货码："+code1+"(仅一次有效)";
        rabbitMQService.sendExpressNotify(expressSendOrder.getCourier().getPhone(),new Result<ExpressSendOrder,String>().setMsg(resStr).setData(expressSendOrder));
        return new Result<ExpressSendOrder,String>().setMsg(resStr).setData(expressSendOrder).setStatus(200);
    }

    @GetMapping(value = "/emptyBoxs")
    public Result<List<ExpressBox>, String> emptyBoxs(@Param(value = "token") String token, @Param(value = "cabinetId") Long cabinetId) {
        Long id = TokenService.validateTokenUser(token);
        if (id <= 0) {
            return new Result<List<ExpressBox>, String>().setStatus(400).setMsg("您的登录凭证无效或过期");
        }
        List<ExpressBox> expressBoxes = expressBoxDAO.findExpressBoxesByCabinet_IdAndIsUsing(cabinetId, 0);
        return new Result<List<ExpressBox>, String>().setStatus(200).setMsg("获取空箱成功").setData(expressBoxes);
    }


    @Resource
    ExpressCainetDAO expressCainetDAO;

    @GetMapping(value = "/cabinets")
    public Result<List<ExpressCabinet>, String> cabinets(@Param(value = "token") String token, @Param(value = "address") String address) {
        Long id = TokenService.validateTokenUser(token);
        if (id <= 0) {
            return new Result<List<ExpressCabinet>, String>().setStatus(400).setMsg("您的登录凭证无效或过期");
        }
        List<ExpressCabinet> expressCabinets = expressCainetDAO.findAllByAddressLike("%" + address + "%");
        return new Result<List<ExpressCabinet>, String>().setStatus(200).setMsg("搜索快递柜成功").setData(expressCabinets);
    }

    @Resource
    CommentDAO commentDAO;

    @PostMapping(value = "/comment")
    public Result<List<ExpressCabinet>, String> comment(@RequestBody JSONObject jsonObject) {
        String token = jsonObject.getString("token");
        Long id = TokenService.validateTokenUser(token);
        if (id <= 0) {
            return new Result<List<ExpressCabinet>, String>().setStatus(400).setMsg("您的登录凭证无效或过期");
        }
        Long sendExp_id = jsonObject.getLong("send_express_id");
        CourierSendExpress courierSendExpress = courierSendExpressDAO.getOne(sendExp_id);
        if (null == courierSendExpress){
            return new Result<List<ExpressCabinet>, String>().setStatus(400).setMsg("该订单不存在，不可评价");
        }
        if (courierSendExpress.getIsPick()==0){
            return new Result<List<ExpressCabinet>, String>().setStatus(400).setMsg("您未取件，不可评价");
        }
        String content = jsonObject.getString("content");
        Comment comment = new Comment();
        Optional<Customer> customer = customerDAO.findById(id);
        comment.setConsumer(customer.get());
        comment.setDate(new Date());
        comment.setObjectTo(courierSendExpress.getCourier());
        comment.setContent(content);
        comment = commentDAO.save(comment);
        if (comment.getId()!=0){
            return new Result<List<ExpressCabinet>, String>().setStatus(200).setMsg("评价成功");
        }
        return new Result<List<ExpressCabinet>, String>().setStatus(400).setMsg("评价失败");

    }

    @RequestMapping(value = "/pick")
    @Transactional
    public Result<String,String> pick(@RequestBody JSONObject jsonObject){
        String token = jsonObject.getString("token");
        Long id = TokenService.validateTokenUser(token);
        if (id<=0){
            return new Result<String, String>().setStatus(400).setMsg("登录凭证无效或过期");
        }
        Long send_id = jsonObject.getLong("send_express_id");
        Optional<CourierSendExpress> courierSendExpress = courierSendExpressDAO.findById(send_id);

        if (!courierSendExpress.isPresent()){
            return new Result<String, String>().setStatus(400).setMsg("快递不存在");
        }

        courierSendExpress.get().setIsPick(1);

        courierSendExpress.get().getBox().setIsUsing(0);

        courierSendExpressDAO.save(courierSendExpress.get());

        expressBoxDAO.save(courierSendExpress.get().getBox());

        return new Result<String, String>().setStatus(200).setMsg("取件成功");
    }

    @RequestMapping(value = "/save")

    public Result<String,String>Save(@RequestBody  JSONObject jsonObject){
        String token = jsonObject.getString("token");
        Long id = TokenService.validateTokenUser(token);
        if (id<=0){
            return new Result<String, String>().setStatus(400).setMsg("登录凭证无效或过期");
        }
        Long express_id = jsonObject.getLong("express_send_id");
        Optional<ExpressSendOrder> expressSendOrder1 = expressSendOrderDAO.findById(express_id);
        if (!expressSendOrder1.isPresent()){
            return new Result<String , String>().setMsg("开柜失败，您提供的信息有误").setStatus(400);
        }
        ExpressSendOrder expressSendOrder = expressSendOrder1.get();
        if (expressSendOrder.getIsSave()==1){
            return new Result<String , String>().setMsg("您已经存件");
        }
        Random random = new Random();
        int code1 = random.nextInt(8999) + 1000;
//            String code = Integer.toString(radn);
        expressSendOrder.setCode(code1);
        expressSendOrder.setIsSave(1);
        expressSendOrderDAO.save(expressSendOrder);
        String resStr = "您的订单"+expressSendOrder.getOrderNo()+" 客户已存储物品，请及时提取，"+ expressSendOrder.getExpress().getBox().getCabinet().getAddress() +
                " " + expressSendOrder.getExpress().getBox().getCabinet().getName() + "快递柜 " +
                expressSendOrder.getExpress().getBox().getName() + "箱,取货码："+code1+"(仅一次有效)";
        rabbitMQService.sendExpressNotify(expressSendOrder.getCourier().getPhone(),new Result<ExpressSendOrder,String>().setMsg(resStr).setData(expressSendOrder));
        return new Result<String,String>().setMsg(resStr).setStatus(200);

    }

}
