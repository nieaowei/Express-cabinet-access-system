package com.ecas.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public final class SmsService {
    private static final String regionId = "cn-hangzhou";
    private static final String domain = "dysmsapi.aliyuncs.com";
    private static final String version = "2017-05-25";
    private static final String action = "SendSms";

    private static final String accessKeyId = "LTAI4GKjitZXpbtnMskCuL8a";
    private static final String accessSecret = "8bNaLayoLtDpr0nuSOPcQrAt0pw3Pd";
    private static final String signName = "疾风速递";
    private static final String templateCode = "SMS_190782054";
    private static final HashMap<String, String> store;
    private static final IAcsClient client;

    static {
        store = new HashMap<>();
        client = new DefaultAcsClient(DefaultProfile.getProfile(regionId, accessKeyId, accessSecret));
    }

    public static Result<Boolean, List<ObjectError>> sendSMS(String phonenum) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(domain);
        request.setSysVersion(version);
        request.setSysAction(action);
        request.putQueryParameter("RegionId", regionId);
        request.putQueryParameter("PhoneNumbers", phonenum);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        Random random = new Random();
        int res = random.nextInt(8999) + 1000;
        String code = Integer.toString(res);
        store.put(phonenum, code);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            JSONObject jsonObject = JSON.parseObject(response.getData());
            if ("OK".equals(jsonObject.getString("Code"))){
                return new Result<Boolean,List<ObjectError>>().setStatus(200).setMsg("发送验证码成功");
            }else if ("isv.BUSINESS_LIMIT_CONTROL".equals(jsonObject.getString("Code"))){
                return new Result<Boolean,List<ObjectError>>().setStatus(400).setMsg("今日短信发送上限，请明天再试");
            }
            return new Result<Boolean,List<ObjectError>>().setStatus(400).setMsg("发送失败");
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result<Boolean,List<ObjectError>>().setStatus(400).setMsg("发送失败");
        }
    }

    public static String getCode(String phonenum) {
        String res = store.get(phonenum);
        if (null == res) {
            return res;
        }
        store.remove(phonenum);
        return res;
    }
}
