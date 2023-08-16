package com.vv.handler.deduplication.build;


import cn.hutool.core.date.DateUtil;
import com.vv.common.domain.TaskInfo;
import com.vv.common.enums.DeduplicationType;
import com.vv.common.utils.AnchorState;
import com.vv.handler.deduplication.DeduplicationParam;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * 频次去重
 * 五分钟一次
 */
@Service
public class FrequencyDeduplicationBuilder extends AbstractDeduplicationBuilder implements Builder{

    public FrequencyDeduplicationBuilder(){
        deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplicationConfig, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getDeduplicationParam(deduplicationType, deduplicationConfig, taskInfo);

        if (Objects.isNull(deduplicationParam)) {
            return null;
        }
        deduplicationParam.setDeduplicationTime((DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000);
        deduplicationParam.setAnchorState(AnchorState.RULE_DEDUPLICATION);
        return deduplicationParam;
    }
}
