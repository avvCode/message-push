package com.vv.handler.reciver.kafka;

import com.vv.handler.utils.GroupIdMappingUtils;
import com.vv.support.constants.MessageQueuePipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;

@Service
@Slf4j
@ConditionalOnProperty(name = "message.push.mq.pipeline", havingValue = MessageQueuePipeline.KAFKA)
public class KafkaReceiverStart {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * receiver的消费方法常量
     */
    private static final String RECEIVER_METHOD_NAME = "KafkaReceiver.consumer";

    /**
     * 下标(用于迭代groupIds位置)
     */
    private static Integer index = 0;

    /**
     * 获取得到所有的groupId
     */
    private static List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();
    /**
     * 创建出所有的Receiver消费者
     */
    @PostConstruct
    public void init(){
        for (int i = 0; i < groupIds.size(); i++) {
            applicationContext.getBean(KafkaReceiver.class);
        }
    }

    /**
     * 给创建出来的所有Receiver消费者指定固定的消费者组id
     */
    @Bean
    public KafkaListenerAnnotationBeanPostProcessor.AnnotationEnhancer groupIdEnhancer(){
        return (attrs, element) -> {
            if (element instanceof Method) {
                String name = ((Method) element).getDeclaringClass().getSimpleName() + "." + ((Method) element).getName();
                if (RECEIVER_METHOD_NAME.equals(name)) {
                    attrs.put("groupId", groupIds.get(index++));
                }
            }
            return attrs;
        };
    }
}
