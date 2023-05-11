package com.vv.support.pending;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;

/**
 * 缓冲区
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class PendingParam <T>{
    /**
     * 阻塞队列
     */
    private BlockingDeque<T> queue;

    /**
     * batch 触发执行的数量阈值【必填】
     */
    private Integer numThreshold;

    /**
     * batch 触发执行的时间阈值，单位毫秒【必填】
     */
    private Long timeThreshold;

    /**
     * 消费线程池实例【必填】
     */
    protected ExecutorService executorService;

}
