package com.vv.support.mq.kafka;

import com.vv.support.constants.MessageQueuePipeline;
import com.vv.support.mq.SendMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@ConditionalOnProperty(name = "message.push.mq.pipeline", havingValue = MessageQueuePipeline.KAFKA)
public class KafkaSendMqServiceImpl implements SendMqService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public void send(String topic, String jsonValue) {
        kafkaTemplate.send(topic,jsonValue);
    }

    @Override
    public void send(String topic, String jsonValue, String tagId) {
        send(topic,jsonValue);
    }
}
