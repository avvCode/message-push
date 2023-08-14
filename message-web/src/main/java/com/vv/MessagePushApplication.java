package com.vv;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.vv.support.mapper")
public class MessagePushApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MessagePushApplication.class, args);
    }
}
