package com.ecas.dao;

import com.ecas.EcasApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {EcasApplication.class})
@WebAppConfiguration
public class ExpressSendOrderDAOTest {

    @Autowired
    ExpressSendOrderDAO expressSendOrderDAO;

    @Test
    public void findByCreateTimeBeforeAndStatus() {
        System.out.println(expressSendOrderDAO.findByCreateTimeBeforeAndStatus(new Date(),0));
    }
}