package com.zlw.mq.topic;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MQFirstTopicConsumer {
    /*
     * 监听和读取通告
     */
    @JmsListener(destination="test-topic",containerFactory = "jmsListenerContainerTopic")
    public void readActiveTopic(String message) {
        System.out.println("First接收到通告:" + message );
        System.out.println("First接收时间：" + new Date());
    }

}
