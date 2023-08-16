package com.vv.service.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import com.vv.common.domain.TaskInfo;
import com.vv.common.enums.ChannelType;
import com.vv.common.enums.MessageType;
import com.vv.common.enums.ResponseCodeEnums;
import com.vv.common.utils.EnumUtil;
import com.vv.common.vo.BasicResult;
import com.vv.service.api.enums.BusinessCode;
import com.vv.service.api.impl.domain.SendTaskModel;
import com.vv.support.constants.MessageQueuePipeline;
import com.vv.support.mq.SendMqService;
import com.vv.support.pipeline.BusinessProcess;
import com.vv.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SendMqAction implements BusinessProcess<SendTaskModel> {

    @Autowired
    private SendMqService sendMqService;

    /**
     * 指定的消息队列类型
     */
    @Value("${message.push.mq.pipeline}")
    private String mqPipeline;
    //Kafka
    @Value("${message.push.business.topic.name}")
    private String sendMessageTopic;

    //rabbitmq
    @Value("${message.push.rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${message.push.rabbitmq.routing.key.prefix}")
    private String routingKeyPrefix;

    //撤回
    @Value("${message.push.business.recall.topic.name}")
    private String recall;




    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();
        try {
            if (BusinessCode.COMMON_SEND.getCode().equals(context.getCode())) {
                String message = JSON.toJSONString(sendTaskModel.getTaskInfo(), SerializerFeature.WriteClassName);
                switch (mqPipeline){
                    case MessageQueuePipeline.KAFKA :
                        SendKafka(message,sendMessageTopic);
                        break;
                    case MessageQueuePipeline.RABBIT_MQ:
                        TaskInfo taskInfo = sendTaskModel.getTaskInfo().iterator().next();
                        ChannelType channelType = EnumUtil.getEnumByCode(taskInfo.getSendChannel(), ChannelType.class);
                        MessageType messageType = EnumUtil.getEnumByCode(taskInfo.getMsgType(), MessageType.class);
                        String routingKey = routingKeyPrefix + "." + channelType.getCodeEn() + "." + messageType.getCodeEn();
                        SendRabbitMq(message,exchangeName,routingKey);
                        break;

                }


            } else if (BusinessCode.RECALL.getCode().equals(context.getCode())) {
                //TODO 消息撤回
                String message = JSON.toJSONString(sendTaskModel.getMessageTemplate(), SerializerFeature.WriteClassName);
                sendMqService.send(recall, message);
            }

        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResult.fail(ResponseCodeEnums.SERVICE_ERROR));
            log.error("send {} fail! e:{},params:{}", mqPipeline, Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(CollUtil.getFirst(sendTaskModel.getTaskInfo().listIterator())));
        }
    }
    public void SendRabbitMq(String message,String exchangeName, String routingKey){
        sendMqService.send(exchangeName,message,routingKey);
    }
    public void SendKafka(String message, String topic){
        sendMqService.send(topic,message);
    }
}
