package com.ecas.validators;

import com.ecas.dao.CustomerDAO;
import com.ecas.entity.Courier;
import com.ecas.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component(value = "CourierValidator")
public class CourierValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Courier.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        System.out.println("valid");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"password","NotNull","密码不能为空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"name","NotNull","姓名不能为空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"phone","NotNull","手机号码不能为空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"company","NotNull","快递公司不能为空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"idCard","NotNull","身份证不能为空");

    }
}
