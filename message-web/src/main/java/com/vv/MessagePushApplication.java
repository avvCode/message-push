package com.vv;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessagePushApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessagePushApplication.class,args);
    }
}
