package com.ecas.validators;

import com.ecas.entity.CourierSendExpress;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
@Component(value = "CourierSendExpressValidator")
public class CourierSendExpressValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return CourierSendExpress.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        System.out.println("CourierSendExpressValidator");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"code","","取货码不能为空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"trackNo","","快件物流号不能为空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"reciverPhone","","收件人手机号码不能为空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"box","","快递箱号不能为空");
    }
}
