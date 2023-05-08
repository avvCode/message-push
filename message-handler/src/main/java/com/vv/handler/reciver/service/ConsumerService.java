package com.vv.handler.reciver.service;


import com.vv.common.domain.TaskInfo;
import com.vv.support.domain.MessageTemplate;

import java.util.List;

public interface ConsumerService {
    /**
     * 发送消息
     */
    void consumer2Send(List<TaskInfo> taskInfoLists);

    /**
     * 撤回消息
     */
    void consumer2recall(MessageTemplate messageTemplate);
}
