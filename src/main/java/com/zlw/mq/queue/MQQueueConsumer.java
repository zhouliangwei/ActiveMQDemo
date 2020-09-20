package com.zlw.mq.queue;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/*
 * 客户控制器
 */
@Component
public class MQQueueConsumer {
    /*
     * 监听和读取消息
     */
    @JmsListener(destination="test-queue",containerFactory = "jmsListenerContainerQueue")
    public void readActiveQueue(String message) {
        System.out.println("接收到消息:" + message );
        System.out.println("接收时间：" + new Date());
    }



}
