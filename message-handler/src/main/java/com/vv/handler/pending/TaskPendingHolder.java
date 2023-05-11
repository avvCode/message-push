package com.vv.handler.pending;

import com.dtp.core.thread.DtpExecutor;
import com.vv.handler.config.HandlerThreadPoolConfig;
import com.vv.handler.utils.GroupIdMappingUtils;
import com.vv.support.utils.ThreadPoolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 消息缓冲区
 *
 * 将所有消费者组都绑定上一个线程池
 */
@Component
public class TaskPendingHolder {
    @Autowired
    private ThreadPoolUtils threadPoolUtils;
    //获取所有的消费者组id
    private List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();

    private Map<String, ExecutorService> taskPendingHolder = new HashMap<>(32);

    @PostConstruct
    public void init(){
        //将消费者组id与线程池关联起来
        for (String groupId : groupIds) {
            DtpExecutor executor = HandlerThreadPoolConfig.getExecutor(groupId);
            threadPoolUtils.register(executor);

            taskPendingHolder.put(groupId, executor);
        }
    }

    public ExecutorService getRoute(String groupId){
        return taskPendingHolder.get(groupId);
    }
}
