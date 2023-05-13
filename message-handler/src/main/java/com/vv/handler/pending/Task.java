package com.vv.handler.pending;

import cn.hutool.core.collection.CollUtil;
import com.vv.common.domain.TaskInfo;
import com.vv.handler.deduplication.DeduplicationRuleService;
import com.vv.handler.handler.HandlerHolder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Accessors(chain = true)
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Task implements Runnable{

    @Autowired
    private DeduplicationRuleService deduplicationRuleService;

    @Autowired
    private HandlerHolder handlerHolder;

    private TaskInfo taskInfo;

    @Override
    public void run() {
        //0.丢弃

        //1.屏蔽

        //2.去重
        if( CollUtil.isNotEmpty(taskInfo.getReceiver())){
            deduplicationRuleService.duplication(taskInfo);
        }
        // 3. 真正发送消息
        if (CollUtil.isNotEmpty(taskInfo.getReceiver())) {
            handlerHolder.route(taskInfo.getSendChannel())
                    .doHandler(taskInfo);
        }
    }
}
