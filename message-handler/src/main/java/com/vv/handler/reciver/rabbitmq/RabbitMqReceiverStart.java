package com.vv.handler.reciver.rabbitmq;

import com.vv.handler.utils.GroupIdMappingUtils;
import com.vv.support.constants.MessageQueuePipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author vv
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "message.push.mq.pipeline", havingValue = MessageQueuePipeline.RABBIT_MQ)
public class RabbitMqReceiverStart {
    /**
     * 上下文容器，用来获取实例
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 编程的方式绑定队列
     */
    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Value("${message.push.rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${message.pusg.rabbitmq.queue.name.prefix}")
    private String queuePrefix;
    @Value("${message.push.rabbitmq.routing.key.prefix}")
    private String routingKeyPrefix;


    /**
     * 获取得到所有的groupId
     */
    private static List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();

    /**
     * 创建出对象，这个是在bean属性注入之后搞定的
     */
    @PostConstruct
    public void init() {

        for (int i = 0; i < groupIds.size(); i++) {
            applicationContext.getBean(RabbitMqReceiver.class);
        }
        //申明交换机
        rabbitAdmin.declareExchange(new TopicExchange(exchangeName, true, false));
        //开始生产队列
        for (String s : groupIds) {
            //队列名称
            String queueName = queuePrefix + "." + s;
            //路由名称
            String routingKey = routingKeyPrefix + "." + s;
            //申明队列
            rabbitAdmin.declareQueue(new Queue(queueName, true));
            //绑定上交换机
            rabbitAdmin.declareBinding(new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, null));
        }
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

}
