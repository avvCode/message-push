package com.vv.support.mq;

public interface SendMqService {
    /**
     * 发送消息
     *
     * @param topic
     * @param jsonValue
     */
    void send(String topic, String jsonValue);
}
