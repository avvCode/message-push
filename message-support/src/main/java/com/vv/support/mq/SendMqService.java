package com.vv.support.mq;

public interface SendMqService {
    /**
     * 一个接口，发送具体看要哪个中间件
     */
    void send(String topic,String jsonValue);

    /**
     * 发送消息
     *
     * @param topic
     * @param jsonValue
     * @param tagId
     */
    void send(String topic, String jsonValue, String tagId);

}
