package com.vv.handler.reciver.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.vv.common.domain.TaskInfo;
import com.vv.handler.reciver.service.ConsumeService;
import com.vv.handler.utils.GroupIdMappingUtils;
import com.vv.support.domain.MessageTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumeServiceImpl implements ConsumeService {
    @Override
    public void consume2Send(List<TaskInfo> taskInfoLists) {
        //获取到对应的消费者组
        String topicGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfoLists.iterator()));
        for (TaskInfo taskInfoList : taskInfoLists) {
            //TODO 将每件消息做成Task，然后交由每个消费者组的线程池去执行任务

        }

    }

    @Override
    public void consume2recall(MessageTemplate messageTemplate) {

    }
}
