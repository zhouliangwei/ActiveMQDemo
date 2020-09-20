package com.zlw.mq.ztest;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import java.util.Date;

@RestController
public class TestController {

    @Autowired
    private TestMQProducer testMQProducer;

    @GetMapping("/testMQ")
    public String testMQ(){
        Destination destination = new ActiveMQQueue("first-queueStep");
        testMQProducer.send(destination);
        System.out.println("第一步开始时间：" + new Date());
        return "成功，请到后台查看信息。";
    }

}
