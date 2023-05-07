package com.vv.service.api.impl.domain;

import com.vv.common.domain.TaskInfo;
import com.vv.service.api.domain.MessageParam;
import com.vv.support.domain.MessageTemplate;
import com.vv.support.pipeline.ProcessModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送消息的模板参数
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SendTaskModel implements ProcessModel {
    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 请求参数
     */
    private List<MessageParam> messageParamList;

    /**
     * 发送任务的信息
     */
    private List<TaskInfo> taskInfo;

    /**
     * 撤回任务的信息
     */
    private MessageTemplate messageTemplate;
}
