package com.vv.support.mq.rabbit;

import com.vv.support.constants.MessageQueuePipeline;
import com.vv.support.mq.SendMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author vv
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "message.push.mq.pipeline", havingValue = MessageQueuePipeline.RABBIT_MQ)
public class RabbitSendMqServiceImpl implements SendMqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Value("${message.push.rabbitmq.exchange.name}")
    private String exchangeName;



    @Override
    public void send(String topic, String jsonValue) {

    }

    @Override
    public void send(String topic, String jsonValue, String routingKey) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, jsonValue);
    }
}