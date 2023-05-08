package com.vv.handler.reciver.service.impl;

import com.vv.common.domain.TaskInfo;
import com.vv.handler.reciver.service.ConsumerService;
import com.vv.support.domain.MessageTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumerServiceImpl implements ConsumerService {
    @Override
    public void consumer2Send(List<TaskInfo> taskInfoLists) {

    }

    @Override
    public void consumer2recall(MessageTemplate messageTemplate) {

    }
}
