package com.vv.service.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.vv.common.constant.MessagePushConstant;
import com.vv.common.enums.ResponseCodeEnums;
import com.vv.common.vo.BasicResult;
import com.vv.service.api.domain.MessageParam;
import com.vv.service.api.impl.domain.SendTaskModel;
import com.vv.support.pipeline.BusinessProcess;
import com.vv.support.pipeline.ProcessContext;
import com.vv.support.pipeline.ProcessModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 前置参数检查
 */
@Slf4j
@Service
public class PreParamCheckAction implements BusinessProcess<SendTaskModel> {
    @Override
    public void process(ProcessContext<SendTaskModel> context) {

        //获取数据模型
        SendTaskModel sendTaskModel = context.getProcessModel();
        //获取消息模板Id
        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        //获取传入的数据
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();

        // 1.没有传入 消息模板Id 或者 messageParam
        if (Objects.isNull(messageTemplateId) || CollUtil.isEmpty(messageParamList)) {
            context.setNeedBreak(true).setResponse(BasicResult.fail(ResponseCodeEnums.CLIENT_BAD_PARAMETERS));
            return;
        }

        // 2.过滤 receiver=null 的 messageParam
        List<MessageParam> resultMessageParamList = messageParamList.stream()
                .filter(messageParam -> !StrUtil.isBlank(messageParam.getReceiver()))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(resultMessageParamList)) {
            context
                    .setNeedBreak(true)
                    .setResponse(BasicResult.fail(ResponseCodeEnums.CLIENT_BAD_PARAMETERS));
            return;
        }

        // 3.过滤receiver大于100的请求
        if (resultMessageParamList
                .stream()
                .anyMatch
                        (messageParam
                                -> messageParam.getReceiver()
                                        .split(StrUtil.COMMA).length >
                                MessagePushConstant.BATCH_RECEIVER_SIZE)) {
            context.setNeedBreak(true).setResponse(BasicResult.fail(ResponseCodeEnums.TOO_MANY_RECEIVER));
            return;
        }
        sendTaskModel.setMessageParamList(resultMessageParamList);
    }
}
