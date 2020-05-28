package com.ecas.configs;

import com.ecas.validators.CourierSendExpressValidator;
import com.ecas.validators.CourierValidator;
import com.ecas.validators.CustomerValidator;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
public class RepositoryRestConfig implements RepositoryRestConfigurer {
//    @Bean
//    @Primary
//    /**
//     * Create a validator to use in bean validation - primary to be able to autowire without qualifier
//     */
//    Validator validator() {
//        return new LocalValidatorFactoryBean();
//    }
//
//    @Bean
//    //the bean name starting with beforeCreate will result into registering the validator before insert
//    public BeforeCreateCourierValidator beforeSaveCustomerValidator() {
//        return new BeforeCreateCourierValidator();
//    }
    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("beforeSave", new CourierValidator());
        validatingListener.addValidator("beforeCreate", new CourierValidator());

        validatingListener.addValidator("beforeSave",new CustomerValidator());
        validatingListener.addValidator("beforeCreate", new CustomerValidator());

        validatingListener.addValidator("beforeSave",new CourierSendExpressValidator());
        validatingListener.addValidator("beforeCreate", new CourierSendExpressValidator());
    }

}
