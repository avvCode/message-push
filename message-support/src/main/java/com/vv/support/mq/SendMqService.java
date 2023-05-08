package com.vv.support.mq;

public interface SendMqService {
    /**
     * 一个接口，发送具体看要哪个中间件
     */
    void send(String topic,String jsonValue);
}
