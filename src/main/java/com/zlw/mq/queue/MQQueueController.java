package com.zlw.mq.queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import java.util.Date;

@RestController
public class MQQueueController {

    @Autowired
    private MQQueueProducer mqProducer;

    @GetMapping("/sendMsg/{info}")
    public String sendQueue(@PathVariable String info) {

        // 创建一个Queue的Destination，参数为Destination的名称
        Destination destination = new ActiveMQQueue("test-queue");
        // 发送消息。参数1：Destination；参数2：消息内容
        mqProducer.send(destination,info);
        System.out.println("即时发送时间：" + new Date());
        return "成功，请到后台查看信息。";
    }

    @GetMapping("/sendMsgDelayOf5s/{info}")
    public String sendQueueDelay(@PathVariable String info) {
        // 创建一个Queue的Destination，参数为Destination的名称
        Destination destination = new ActiveMQQueue("test-queue");
        // 发送消息。参数1：Destination；参数2：消息内容
        mqProducer.sendOfDelay5Seconds(destination,info);
        System.out.println("延时5秒发送时间：" + new Date());
        return "成功，请到后台查看信息。";
    }

    @GetMapping("/sendMsgRepeat/{info}")
    public String sendQueueRepeat(@PathVariable String info) {
        // 创建一个Queue的Destination，参数为Destination的名称
        Destination destination = new ActiveMQQueue("test-queue");
        // 发送消息。参数1：Destination；参数2：消息内容
        mqProducer.sendOfRepeat(destination,info);
        System.out.println("循环发送时间：" + new Date());
        return "成功，请到后台查看信息。";
    }

    @GetMapping("/sendMsgCron/{info}")
    public String sendQueueCron(@PathVariable String info) {
        // 创建一个Queue的Destination，参数为Destination的名称
        Destination destination = new ActiveMQQueue("test-queue");
        // 发送消息。参数1：Destination；参数2：消息内容
        mqProducer.sendOfCron(destination,info);
        System.out.println("定时发送时间：" + new Date());
        return "成功，请到后台查看信息。";
    }

}
