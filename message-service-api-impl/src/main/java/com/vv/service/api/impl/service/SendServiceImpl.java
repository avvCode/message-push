package com.vv.service.api.impl.service;

import com.vv.common.vo.BasicResult;
import com.vv.service.api.domain.BatchSendRequest;
import com.vv.service.api.domain.SendRequest;
import com.vv.service.api.domain.SendResponse;
import com.vv.service.api.impl.domain.SendTaskModel;
import com.vv.service.api.service.SendService;
import com.vv.support.pipeline.ProcessContext;
import com.vv.support.pipeline.ProcessController;
import com.vv.support.pipeline.ProcessModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SendServiceImpl implements SendService {
    @Autowired
    private ProcessController processController;



    @Override
    public SendResponse send(SendRequest sendRequest) {

        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .messageParamList(Collections.singletonList(sendRequest.getMessageParam()))
                .build();

        ProcessContext context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResult.success()).build();

        ProcessContext process = processController.process(context);

        return new SendResponse(process.getResponse().getCode(), process.getResponse().getMsg());
    }

    @Override
    public SendResponse batchSend(BatchSendRequest batchSendRequest) {
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(batchSendRequest.getMessageTemplateId())
                .messageParamList(batchSendRequest.getMessageParamList())
                .build();

        ProcessContext context = ProcessContext.builder()
                .code(batchSendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResult.success()).build();

        ProcessContext process = processController.process(context);

        return new SendResponse(process.getResponse().getCode(), process.getResponse().getMsg());
    }
}
