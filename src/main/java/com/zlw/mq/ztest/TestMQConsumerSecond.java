package com.zlw.mq.ztest;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TestMQConsumerSecond {

    @JmsListener(destination = "second-topicStep",containerFactory = "jmsListenerContainerTopic")
    public void readActiveTopic(){
        System.out.println("这里是FirstTopic，收到指令时间："+ new Date());
    }

}
