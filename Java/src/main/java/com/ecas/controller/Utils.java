package com.ecas.controller;

import com.alibaba.fastjson.JSONObject;
import com.ecas.utils.Result;
import com.ecas.utils.SmsService;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Utils {
    @PostMapping(value = "/sendSMS")
    public Result<Boolean, List<ObjectError>> sendSMS(@RequestBody JSONObject data) {
        String phone = data.getString("phone");
        if (null == phone) {
            return new Result<Boolean,List<ObjectError>>().setMsg("手机号不能为空").setStatus(400);
        }
        return SmsService.sendSMS(data.getString("phone"));
    }


}
