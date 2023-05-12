package com.vv.handler.deduplication.build;

import com.vv.common.domain.TaskInfo;
import com.vv.common.enums.DeduplicationType;
import com.vv.common.utils.AnchorState;
import com.vv.handler.deduplication.DeduplicationParam;

import java.util.Objects;


/**
 * 内容去重
 * 一天5次
 */
public class ContentDeduplicationBuilder extends AbstractDeduplicationBuilder implements Builder{

    public ContentDeduplicationBuilder(){
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplicationConfig, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getDeduplicationParam(deduplicationType, deduplicationConfig, taskInfo);
        if (Objects.isNull(deduplicationParam)) {
            return null;
        }
        //日志埋点，消息被去重
        deduplicationParam.setAnchorState(AnchorState.CONTENT_DEDUPLICATION);
        return deduplicationParam;
    }
}
