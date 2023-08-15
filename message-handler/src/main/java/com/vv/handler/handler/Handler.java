package com.vv.handler.handler;

import com.vv.common.domain.TaskInfo;
import com.vv.support.domain.MessageTemplate;

/**
 *
 * @author vv
 */
public interface Handler {
    //处理发送消息
    void doHandler(TaskInfo taskInfo);


    //处理撤回消息
    void recall(MessageTemplate messageTemplate);
}
