package com.ecas.utils;

import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import java.util.LinkedList;
import java.util.List;

public final class ValidErrorHandler {
    public static <T> List<String> handleErrors(Validator validator, T tClass) {
        DataBinder binder = new DataBinder(tClass);
        binder.setValidator(validator);
        binder.validate();
        Errors results = binder.getBindingResult();
        List<String> res = new LinkedList<>();
        List<FieldError> in = results.getFieldErrors();
        for (FieldError fieldError : in) {
            res.add(fieldError.getDefaultMessage());
        }
        return res;
    }
}
