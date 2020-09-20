package com.zlw.mq.ztest;


import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import java.util.Date;


@Component
public class TestMQConsumerFirst {
    @Autowired
    private TestMQProducer testMQProducer;

    @JmsListener(destination = "first-queueStep",containerFactory = "jmsListenerContainerQueue")
    public void readActiveQueue() throws Exception {
        System.out.println("第一步工作中...");
        Thread.sleep(2000);
        System.out.println("第一步Queue已经做完，现在开始第二步Topic,开始时间："+ new Date());
        Destination destination = new ActiveMQTopic("second-topicStep");
        testMQProducer.send(destination);
    }

}
