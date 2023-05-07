package com.vv.support.pipeline;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 职责链控制器
 */
@Data
@Slf4j
public class ProcessController {
    // 不同的code 对应不同的责任链
    private Map<String, ProcessTemplate> templateConfig = null;


}
