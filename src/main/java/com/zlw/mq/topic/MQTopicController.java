package com.zlw.mq.topic;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import java.util.Date;

@RestController
public class MQTopicController {

    @Autowired
    private MQTopicProducer mqProducer;

    @GetMapping("/sendTopic/{topic}")
    public String sendTopic(@PathVariable String topic){
        // 创建一个topic的Destination，参数为Destination的名称
        Destination destination = new ActiveMQTopic("test-topic");
        // 发送消息。参数1：Destination；参数2：消息内容
        mqProducer.sendTopic(destination,topic);
        System.out.println("即时发送通告时间：" + new Date()+ "  ，即时发送通告内容：" + topic);
        return "成功，请到后台查看信息。";
    }
}
