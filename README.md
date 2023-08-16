# Message-Push 消息推送平台

## 项目意义
各大平台都有给出消息推送的接口，假如有项目想单独接入就需要自己去写一个一个接口去适配接口，而公司中又不仅仅只有一个项目或者模块去使用，这个时候，建立一个统一的消息推送平台就显得十分有意义了...
## 项目技术栈
* Java 8 
* SpringBoot 2.x 
* Mybatis-plus ORM框架
* MySQL 8.x 数据库
* Redis 缓存
* RabbitMQ 消息队列 + Zeekeepr 注册中心 
* Apollo配置中心
* Dynamic-TP 动态线程池(需搭配配置中心Apollo/Nacos)
* Docker + Docker-Compose 部署
## 项目架构

## 项目模块
* message-push-common 项目通用枚举、实体类
* message-push-api 接口定义层（send 与 batchSend）
* message-push-api-impl 接口实现层
* message-push-handler 将消息下发到具体平台
* message-push-web 对外提供接口Crud
* message-push-support 对接三方服务（Redis、RabbitMQ、Dynamic-TP……）

## 项目技术特性
1. 采用职责链设计模式处理传入参数
2. 采用单通道接收消息 --> 多个消费者组消费消息 （每个消费者组都配备一个线程池）
3. 采用Redis + lua脚本 + 模板设计模式实现平台消息去重

## 目前接入的平台
1. 邮箱Email
2. 腾讯云手机短信
