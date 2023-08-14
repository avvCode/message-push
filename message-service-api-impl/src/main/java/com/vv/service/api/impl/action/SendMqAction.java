package com.vv.service.api.impl.action;

import com.vv.service.api.impl.domain.SendTaskModel;
import com.vv.support.mq.SendMqService;
import com.vv.support.pipeline.BusinessProcess;
import com.vv.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SendMqAction implements BusinessProcess<SendTaskModel> {
    @Autowired
    private SendMqService sendMqService;
    @Override
    public void process(ProcessContext<SendTaskModel> context) {

    }
}
