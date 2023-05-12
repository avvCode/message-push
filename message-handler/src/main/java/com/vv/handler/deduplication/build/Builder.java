package com.vv.handler.deduplication.build;

import com.vv.common.domain.TaskInfo;
import com.vv.handler.deduplication.DeduplicationParam;

public interface Builder {

    String DEDUPLICATION_CONFIG_PRE = "deduplication_";

    /**
     * 根据配置构建去重参数
     *
     * @param deduplicationConfig
     * @param taskInfo
     * @return
     */
    DeduplicationParam build(String deduplicationConfig, TaskInfo taskInfo);
}
