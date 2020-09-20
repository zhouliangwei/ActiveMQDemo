package com.zlw.mq.ztest;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TestMQConsumerThird {
    @JmsListener(destination = "second-topicStep",containerFactory = "jmsListenerContainerTopic")
    public void readActiveTopic() {
        System.out.println("这里是SecondTopic，收到指令时间："+ new Date());
    }
}
