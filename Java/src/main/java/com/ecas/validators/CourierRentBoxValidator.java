package com.ecas.validators;

import com.ecas.entity.CourierRentBox;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CourierRentBoxValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return CourierRentBox.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        System.out.println("CourierRentBoxValidator");
    }
}
