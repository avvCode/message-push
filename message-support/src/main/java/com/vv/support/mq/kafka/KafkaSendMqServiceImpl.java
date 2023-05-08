package com.vv.support.mq.kafka;

import com.vv.support.mq.SendMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSendMqServiceImpl implements SendMqService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public void send(String topic, String jsonValue) {
        kafkaTemplate.send(topic,jsonValue);
    }
}
