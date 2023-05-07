package com.vv.service.api.impl.service;

import com.vv.common.vo.BasicResult;
import com.vv.service.api.domain.SendRequest;
import com.vv.service.api.domain.SendResponse;
import com.vv.service.api.impl.domain.SendTaskModel;
import com.vv.service.api.service.RecallService;
import com.vv.support.pipeline.ProcessContext;
import com.vv.support.pipeline.ProcessController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecallServiceImpl implements RecallService {
    @Autowired
    private ProcessController processController;

    @Override
    public SendResponse recall(SendRequest sendRequest) {
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .build();
        ProcessContext processContext = ProcessContext.builder()
                .code(sendRequest.getCode())
                .needBreak(false)
                .processModel(sendTaskModel)
                .response(BasicResult.success())
                .build();
        ProcessContext process = processController.process(processContext);
        return new SendResponse(process.getResponse().getCode(),process.getResponse().getMsg());
    }
}
