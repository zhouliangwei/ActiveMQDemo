package com.zlw.mq.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

@Component
public class MQTopicProducer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    /*
     * 消息生产者
     * 即时发送消息
     */
    public void sendTopic(Destination destination, String message){
        jmsMessagingTemplate.convertAndSend(destination,message);
    }


}
