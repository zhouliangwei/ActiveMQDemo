package com.zlw.mq.queue;

import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.TextMessage;

/*
 * 队列消息控制器
 */
@Component
public class MQQueueProducer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    /*
     * 消息生产者
     * 即时发送消息
     */
    public void send(Destination destination, String message){
        jmsMessagingTemplate.convertAndSend(destination,message);
    }

    /*
     * 消息生产者
     * 延迟、定时发送消息
     *
     * AMQ_SCHEDULED_DELAY 消息延迟时间单位：毫秒 LONG
     * AMQ_SCHEDULED_PERIOD 消息发送周期单位时间：毫秒 LONG
     * AMQ_SCHEDULED_REPEAT 消息重复发送次数 INT
     * AMQ_SCHEDULED_CRON 使用Cron 表达式 设置定时发送 String
     * 四个选项可以混合使用，但是CRON执行顺序大于DELAY。
     */
    public void sendOfDelay5Seconds(Destination destination, String message){
        jmsTemplate.send(destination,session ->{

            TextMessage textMessage = session.createTextMessage(message);
            // 设置延时时间【关键】
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5000);// 延时5秒
            return textMessage;
        });
    }

    //使用例子二：重复发送3次，每次之间间隔2秒
    public void sendOfRepeat(Destination destination, String message){
        jmsTemplate.send(destination,session ->{
            TextMessage textMessage = session.createTextMessage(message);
            // 设置重复次数【关键】，注意这里是 setIntProperty
            textMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 3);// 重复3次，共4次
            // 设置周期时间，注意这里是 setLongProperty
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD,2000);
            return textMessage;
        });
    }

    /**
     *   # CRON文件格式说明
     *   #  minute（0 - 59）
     *   # |  hour（0 - 23）
     *   # | |  day of month（1 - 31） 几号
     *   # | | |  month（1 - 12）
     *   # | | | |  day of week（0 - 7，星期几，星期日=0或7）
     *   # | | | | |
     &   # * * * * * 执行的命令
     * @param destination
     * @param message
     */
    public void sendOfCron(Destination destination, String message){
        jmsTemplate.send(destination,session ->{
            TextMessage textMessage = session.createTextMessage(message);
            // 分钟 小时 日期 月份 星期  每个小时的第21分钟执行
            textMessage.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON,"25 * * * *");
            return textMessage;
        });
    }




}
