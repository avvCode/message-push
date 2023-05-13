package com.vv.handler.handler;


import com.vv.common.domain.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 顶级处理抽象类
 */
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
}
