package com.vv.handler.reciver.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.vv.common.domain.TaskInfo;
import com.vv.handler.handler.HandlerHolder;
import com.vv.handler.pending.Task;
import com.vv.handler.pending.TaskPendingHolder;
import com.vv.handler.reciver.service.ConsumeService;
import com.vv.handler.utils.GroupIdMappingUtils;
import com.vv.support.domain.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumeServiceImpl implements ConsumeService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TaskPendingHolder taskPendingHolder;

    @Autowired
    private HandlerHolder handlerHolder;

    @Override
    public void consume2Send(List<TaskInfo> taskInfoLists) {
        //获取到对应的消费者组
        String topicGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfoLists.iterator()));
        for (TaskInfo taskInfo : taskInfoLists) {
            //创建一个任务对象
            Task task = applicationContext
                    .getBean(Task.class)
                    .setTaskInfo(taskInfo);
            taskPendingHolder
                    .route(topicGroupId)
                    .execute(task);
        }

    }

    @Override
    public void consume2recall(MessageTemplate messageTemplate) {
        handlerHolder
                .route(messageTemplate.getSendChannel())
                .recall(messageTemplate);
    }
}
