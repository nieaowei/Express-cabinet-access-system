package com.ecas.dao;

import com.ecas.entity.Customer;
import com.ecas.EcasApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {EcasApplication.class})
@WebAppConfiguration
public class CustomerDAOTest {

    @Autowired
    CustomerDAO customerDAO;
    @Test
    public void test(){
    }
}