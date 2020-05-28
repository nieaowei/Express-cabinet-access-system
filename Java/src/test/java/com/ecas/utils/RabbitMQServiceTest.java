package com.ecas.utils;

import com.ecas.EcasApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {EcasApplication.class})
@WebAppConfiguration
public class RabbitMQServiceTest {
    @Autowired
    RabbitMQService rabbitMQService;
    @Test
    public void sendNotify() throws Exception{
        while (true){
            rabbitMQService.sendExpressNotify("13060955245",new Result<>().setMsg("12345"));
            Thread.sleep(3000);
        }
    }
}