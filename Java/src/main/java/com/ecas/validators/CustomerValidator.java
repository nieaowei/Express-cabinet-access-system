package com.ecas.validators;

import com.ecas.entity.Customer;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component(value = "CustomerValidator")
public class CustomerValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Customer.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"password","NotNull","密码不能为空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"name","NotNull","姓名不能为空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"phone","NotNull","手机号码不能为空");

    }
}
