package com.vv.handler.pending;

import com.vv.common.domain.TaskInfo;
import com.vv.handler.handler.HandlerHolder;
import jdk.internal.org.objectweb.asm.Handle;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Accessors(chain = true)
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Task implements Runnable{

    private HandlerHolder handlerHolder;

    private TaskInfo taskInfo;

    @Override
    public void run() {
        //0.丢弃

        //1.屏蔽

        //2.去重

        //3.发送消息
    }
}
