package com.zlw.mq.ztest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

@Component
public class TestMQProducer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    public void send(Destination destination){
        // covertAndSend必须有一个payload荷载，不能为空
        String message = "aa";
        jmsMessagingTemplate.convertAndSend(destination,message);
    }
}
