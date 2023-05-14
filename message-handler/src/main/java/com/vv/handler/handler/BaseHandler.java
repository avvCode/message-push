package com.vv.handler.handler;


import com.vv.common.domain.TaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.Signed;

/**
 * 顶级处理抽象类
 */
@Slf4j
public abstract class BaseHandler implements Handler{
    /**
     * 渠道Id
     */
    protected Integer channelCode;

    @Autowired
    protected HandlerHolder handlerHolder;

    @PostConstruct
    public void init(){
        handlerHolder.putHandler(channelCode,this);
    }
    /**
     * 统一处理的handler接口
     *
     * @param taskInfo
     * @return
     */
    public abstract boolean handler(TaskInfo taskInfo);

    /**
     * 预测在此处做限流处理
     * @param taskInfo
     */
    @Override
    public void doHandler(TaskInfo taskInfo) {
        if (handler(taskInfo)) {}
        return;
    }
}
