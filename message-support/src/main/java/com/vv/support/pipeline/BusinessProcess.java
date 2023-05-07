package com.vv.support.pipeline;

/**
 * 业务执行器
 * 职责链中每个节点都要实现这个接口
 */
public interface BusinessProcess<T extends ProcessModel>{
    void process(ProcessContext<T> context);
}
