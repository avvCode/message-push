package com.vv.handler.reciver.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.vv.common.domain.TaskInfo;
import com.vv.support.constants.MessageQueuePipeline;
import com.vv.handler.reciver.service.ConsumeService;
import com.vv.support.domain.MessageTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author vv
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ConditionalOnProperty(name = "message.push.mq.pipeline", havingValue = MessageQueuePipeline.RABBIT_MQ)
public class RabbitMqReceiver {

    private static final String MSG_TYPE_SEND = "send";
    private static final String MSG_TYPE_RECALL = "recall";

    @Autowired
    private ConsumeService consumeService;

    @RabbitListener(queues = "#{groupIdMappingUtils.getAllGroupIds().get()}")
    public void onMessage(Message message) {
        String messageType = message.getMessageProperties().getHeader("messageType");
        byte[] body = message.getBody();
        String messageContent = new String(body);
        if (StringUtils.isBlank(messageContent)) {
            return;
        }
        if (MSG_TYPE_SEND.equals(messageType)) {
            // 处理发送消息
            List<TaskInfo> taskInfoLists = JSON.parseArray(messageContent, TaskInfo.class);
            consumeService.consume2Send(taskInfoLists);
        } else if (MSG_TYPE_RECALL.equals(messageType)) {
            // 处理撤回消息
            MessageTemplate messageTemplate = JSON.parseObject(messageContent, MessageTemplate.class);
            consumeService.consume2recall(messageTemplate);
        }

    }

}
