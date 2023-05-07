package com.vv.support.pipeline;


import com.vv.common.vo.BasicResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 职责链上下文
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class ProcessContext<T extends ProcessModel> {
    // 标识责任链的code
    private String code;

    // 存储上下文的真正载体
    private T processModel;

    // 责任链中断的标识
    private Boolean needBreak = false;

    //结果
    private BasicResult response;
}
