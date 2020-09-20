package com.zlw.mq.topic;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MQSecondTopicConsumer {
    /*
     * 监听和读取通告
     */
    @JmsListener(destination="test-topic",containerFactory = "jmsListenerContainerTopic")
    public void readActiveTopic(String message) {
        System.out.println("Second接收到通告:" + message );
        System.out.println("Second接收时间：" + new Date());
    }

}
