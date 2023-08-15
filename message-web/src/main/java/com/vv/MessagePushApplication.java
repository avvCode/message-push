package com.vv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@MapperScan("com.vv.support.mapper")
public class MessagePushApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MessagePushApplication.class, args);
    }
}
