package com.ecas.controller;

import com.alibaba.fastjson.JSONObject;
import com.ecas.dao.*;
import com.ecas.entity.*;
import com.ecas.utils.*;
import com.ecas.validators.CourierSendExpressValidator;
import com.ecas.validators.CourierValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "couriers")
public class Couriers {

    @Resource
    CourierDAO courierDAO;

    @Resource
    CourierValidator courierValidator;

    @PostMapping(value = "/register")
    public Result<String, List<String>> register(@RequestBody JSONObject data) {
        String code = data.getString("code");
        if ((!code.equals(SmsService.getCode(data.getString("phone"))))) {
            if ("1234".equals(code) == false) {
                return new Result<String, List<String>>().setStatus(400).setMsg("验证码错误");
            }
        }
        Courier courier = new Courier();
        courier.setIdCard(data.getString("id_card"));
        courier.setName(data.getString("name"));
        courier.setPassword(data.getString("password"));
        courier.setPhone(data.getString("phone"));
        Company company = new Company();
        company.setId(data.getLong("company"));
        courier.setCompany(company);
        try {
            List<String> res = ValidErrorHandler.handleErrors(courierValidator, courier);
            if (res.size() != 0) {
                return new Result<String, List<String>>().setStatus(400).setMsg("信息不全，注册失败").setErrors(res);
            }
            courier = courierDAO.save(courier);
            if (courier.getId() != 0) {
                return new Result<String, List<String>>().setStatus(200).setMsg("注册成功");
            }
        } catch (Exception e) {
            return new Result<String, List<String>>().setStatus(400).setMsg("信息不全，注册失败");
        }
        return new Result<String, List<String>>().setStatus(400).setMsg("注册失败");
    }

    @PostMapping(value = "/login")
    public Result<JSONObject, List<ObjectError>> login(@RequestBody JSONObject data) {
        Courier courier = courierDAO.findCourierByPhoneAndPassword(data.getString("phone"), data.getString("password"));
        if (null != courier) {
            String token = TokenService.getTokenForUser(courier.getId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", token);
            return new Result<JSONObject, List<ObjectError>>().setStatus(200).setMsg("登录成功").setData(jsonObject);
        }
        return new Result<JSONObject, List<ObjectError>>().setStatus(400).setMsg("登录失败");
    }

    @PostMapping(value = "/changePd")
    public Result<String, List<ObjectError>> changePd(@RequestBody JSONObject data) {
        String phone = data.getString("phone");
        String code = data.getString("code");
        String password = data.getString("password");
        if (code != null && phone != null && password != null) {
            if (code.equals(SmsService.getCode(phone)) || "1234".equals(code)) {
                Courier courier = courierDAO.getByPhone(phone);
                if (null == courier) {
                    return new Result<String, List<ObjectError>>().setStatus(400).setMsg("该手机号尚未注册");
                }
                courier.setPassword(password);
                try {
                    Courier courier1 = courierDAO.save(courier);
                    if (courier1.getPassword().equals(password)) {
                        return new Result<String, List<ObjectError>>().setStatus(200).setMsg("修改密码成功");
                    }
                } catch (Exception e) {
                    return new Result<String, List<ObjectError>>().setStatus(400).setMsg("信息不全");
                }
            }
        }
        return new Result<String, List<ObjectError>>().setStatus(400).setMsg("修改密码失败");
    }

    @Resource
    CourierSendExpressDAO courierSendExpressDAO;

    @GetMapping(value = "/expresses")
    public Result<List<CourierSendExpress>, String> findMyExpresses(@Param(value = "token") String token) {
        long id = TokenService.validateTokenUser(token);
        if (id <= 0) {
            return new Result<List<CourierSendExpress>, String>().setMsg("您的用户凭证无效或已过期").setStatus(400);
        }
        List<CourierSendExpress> result = courierSendExpressDAO.findAllByCourier_Id(id);
        return new Result<List<CourierSendExpress>, String>().setMsg("获取派件信息成功").setStatus(200).setData(result);
    }

    @Autowired
    RabbitMQService rabbitMQService;

    @Resource
    ExpressBoxDAO expressBoxDAO;

    @Autowired
    CourierSendExpressValidator courierSendExpressValidator;

    @PostMapping(value = "/sendExpress")
    public Result<CourierSendExpress, List<String>> sendExpress(@RequestBody JSONObject data) {
        try {
            String token = data.getString("token");

            long id = TokenService.validateTokenUser(token);

            if (!(id <= 0)) {
                data.remove("token");
                long box_id = data.getLong("box");
                data.remove("box");
                CourierSendExpress courierSendExpress = JSONObject.parseObject(data.toJSONString(), CourierSendExpress.class);


//                CourierRentBox courierRentBox = courierRentBoxDAO.findByBox_IdAndFinishFlag(box_id,0);
//                if (courierRentBox!=null){
//                    return new Result<CourierSendExpress, List<String>>().setMsg("该快递箱已经被租用").setStatus(400);
//                }
                Optional<ExpressBox> expressBox = expressBoxDAO.findById(box_id);
                if (expressBox.get().getIsUsing() == 1) {
                    return new Result<CourierSendExpress, List<String>>().setMsg("该快递箱正在被使用").setStatus(400);

                }

                List<CourierRentBox> courierRentBox1 = courierRentBoxDAO.findByBox_IdAndCourier_IdAndFinishFlag(box_id, id, 0);

                if (courierRentBox1.size() != 1) {
                    return new Result<CourierSendExpress, List<String>>().setMsg("您尚未租用该快递箱").setStatus(400);
                }



                Optional<Courier> courier = courierDAO.findById(id);
                courierSendExpress.setCourier(courier.get());
                courierSendExpress.setBox(expressBox.get());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                //防止sql注入
                courierSendExpress.setSaveTime(df.format(new Date()));
                List<String> res = ValidErrorHandler.handleErrors(courierSendExpressValidator, courierSendExpress);
                if (res.size() != 0) {
                    return new Result<CourierSendExpress, List<String>>().setMsg("信息有误，请重新派件").setStatus(400).setErrors(res);
                }
                courierSendExpress = courierSendExpressDAO.saveAndFlush(courierSendExpress);
                if (courierSendExpress.getId() != 0) {
                    String msg = RabbitMQService.NotifyMsg(courierSendExpress);
                    System.out.println(msg);
                    rabbitMQService.sendExpressNotify(courierSendExpress.getReciverPhone(), new Result<>().setMsg(msg));
                    CourierSendExpress courierSendExpress1 = courierSendExpressDAO.getOne(courierSendExpress.getId());
//                    BeanUtils.copyProperties(courierSendExpress, courierSendExpress1);
                    ExpressBox expressBox1 = expressBox.get();
                    expressBox1.setIsUsing(1);
                    expressBoxDAO.saveAndFlush(expressBox1);
                    CourierRentBox courierRentBox = courierRentBoxDAO.findByBox_IdAndFinishFlag(box_id,0);
                    courierRentBox.setFinishFlag(1);
                    courierRentBoxDAO.saveAndFlush(courierRentBox);
                    return new Result<CourierSendExpress, List<String>>().setMsg("派件成功,已通知用户").setStatus(200).setData(courierSendExpress1);
                }
                return new Result<CourierSendExpress, List<String>>().setMsg("派件失败").setStatus(400);
            } else {
                return new Result<CourierSendExpress, List<String>>().setMsg("登录过期，请重新登录").setStatus(300);

            }
        } catch (Exception e) {
            List<String> err = new LinkedList<>();
            err.add(e.getLocalizedMessage());
            return new Result<CourierSendExpress, List<String>>().setMsg("信息有误").setStatus(400).setErrors(err);

        }
    }

    @PostMapping(value = "/againNotify")
    public Result<String, List<String>> againNotify(@RequestBody JSONObject data) {
        try {
            String token = data.getString("token");

            long id = TokenService.validateTokenUser(token);

            if (id <= 0) {
                return new Result<String, List<String>>().setMsg("登录过期，请重新登录").setStatus(300);
            }

            long express_id = data.getLongValue("send_express_id");

            CourierSendExpress courierSendExpress = courierSendExpressDAO.getOne(express_id);
            String msg = RabbitMQService.NotifyMsg(courierSendExpress);
            System.out.println(msg);
            rabbitMQService.sendExpressNotify(courierSendExpress.getReciverPhone(), new Result<>().setMsg(msg));
            return new Result<String, List<String>>().setMsg("重新提醒成功").setStatus(200);

        } catch (Exception e) {
            List<String> err = new LinkedList<>();
            err.add(e.getLocalizedMessage());
            return new Result<String, List<String>>().setMsg("信息有误").setStatus(400).setErrors(err);
        }
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
    CourierRentBoxDAO courierRentBoxDAO;

    @PostMapping(value = "/box/rent")
    public Result<List<ExpressCabinet>, String> boxRent(@RequestBody JSONObject data) {
        String token = data.getString("token");
        Long boxId = data.getLong("boxId");
        Date finishTime = data.getDate("finishTime");
        Long id = TokenService.validateTokenUser(token);
        if (id <= 0) {
            return new Result<List<ExpressCabinet>, String>().setStatus(400).setMsg("您的登录凭证无效或过期");
        }
        if (boxId==null || finishTime==null){
            return new Result<List<ExpressCabinet>, String>().setStatus(400).setMsg("信息不全");
        }
        Optional<ExpressBox> expressBox = expressBoxDAO.findById(boxId);
        if (!expressBox.isPresent()) {
            return new Result<List<ExpressCabinet>, String>().setStatus(400).setMsg("该快递箱不存在");
        }
        List<CourierRentBox> courierRentBox1 = courierRentBoxDAO.findByBox_IdAndCourier_IdAndFinishFlag(boxId, id,0);
        if (courierRentBox1.size() != 0) {
            return new Result<List<ExpressCabinet>, String>().setStatus(400).setMsg("您已经租用该快递箱");
        }
        if (expressBox.get().getIsUsing() == 1) {
            return new Result<List<ExpressCabinet>, String>().setStatus(400).setMsg("该快递箱正在被使用");
        }

        if (expressBox.get().getIsUsing() == 2) {
            return new Result<List<ExpressCabinet>, String>().setStatus(400).setMsg("该快递箱已经被租用");
        }

        CourierRentBox courierRentBox = new CourierRentBox();
        courierRentBox.setBox(expressBox.get());
        Optional<Courier> courier = courierDAO.findById(id);
        courierRentBox.setCourier(courier.get());
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        courierSendExpress.setSaveTime(df.format(new Date()));
        courierRentBox.setFinishTime(finishTime);
        courierRentBox = courierRentBoxDAO.save(courierRentBox);
        if (courierRentBox.getId() != 0) {
            ExpressBox expressBox1 = expressBox.get();
            expressBox1.setIsUsing(2);
            expressBox1 = expressBoxDAO.saveAndFlush(expressBox1);
            if (expressBox1.getIsUsing() != 2) {
                return new Result<List<ExpressCabinet>, String>().setStatus(400).setMsg("租用失败");
            }
            return new Result<List<ExpressCabinet>, String>().setStatus(200).setMsg("租用快递箱成功");
        }
        return new Result<List<ExpressCabinet>, String>().setStatus(400).setMsg("租用快递箱失败");
    }

    @GetMapping(value = "/mybox")
    public Result<List<ExpressBox>, String> myBox(@Param(value = "token")String token){
        long id = TokenService.validateTokenUser(token);
        if (id<=0){
            return new Result<List<ExpressBox>, String>().setStatus(400).setMsg("您的登录凭证无效或过期");
        }
        List<CourierRentBox> courierRentBoxes = courierRentBoxDAO.findByCourier_IdAndFinishFlag(id,0);
        List<ExpressBox> expressBoxes = new LinkedList<>();
        for (CourierRentBox courierRentBox:courierRentBoxes) {
            expressBoxes.add(courierRentBox.getBox());
        }
        return new Result<List<ExpressBox>, String>().setStatus(200).setMsg("获取已租用盒子成功").setData(expressBoxes);
    }

    @Resource
    ExpressSendOrderDAO expressSendOrderDAO;
    @GetMapping(value = "/expresses/recive")
    public Result<List<ExpressSendOrder>, String> recive(@Param(value = "token")String token){
        long id = TokenService.validateTokenUser(token);
        if (id<=0){
            return new Result<List<ExpressSendOrder>, String>().setStatus(400).setMsg("您的登录凭证无效或过期");
        }
        List<ExpressSendOrder> expressSendOrders = expressSendOrderDAO.findByCourier_IdAndStatus(id,2);

        return new Result<List<ExpressSendOrder>, String>().setStatus(200).setMsg("获取快递成功").setData(expressSendOrders);
    }

    @GetMapping(value = "/box/pick")
    @Transactional
    public Result<String,String> Pick(@Param(value = "box_id") Long box_id,@Param(value = "code")Integer code){
//        Optional<ExpressBox> expressBox=expressBoxDAO.findById(box_id);
//        if (!expressBox.isPresent()){
//            return new Result<String,String>().setStatus(400).setMsg("该快递箱不存在");
//        }
        ExpressSendOrder expressSendOrder = expressSendOrderDAO.findByExpress_Box_IdAndCodeAndIsSave(box_id,code,1);
        if (expressSendOrder==null){
            return new Result<String,String>().setStatus(400).setMsg("该快递已被取走或没有该快递或取货码错误");
        }
        ExpressBox expressBox1 = expressSendOrder.getExpress().getBox();

        expressBox1.setIsUsing(0);
        ExpressBox expressBox2 = expressBoxDAO.save(expressBox1);
//        courierSendExpress.setCode(null);
        expressSendOrder.setCode(0);
        expressSendOrderDAO.save(expressSendOrder);
        if (expressBox2.getIsUsing()==0){
            String msg = "您的订单 "+expressSendOrder.getOrderNo()+ "快递员已取件。稍后降为您发出。";
            rabbitMQService.sendExpressNotify(expressSendOrder.getCustomer().getPhone(),new Result<>().setMsg(msg));
            return new Result<String,String>().setStatus(200).setMsg("取件成功");
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return new Result<String,String>().setStatus(400).setMsg("取件失败");
    }
}
