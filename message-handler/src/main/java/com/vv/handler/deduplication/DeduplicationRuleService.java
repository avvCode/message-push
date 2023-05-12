package com.vv.handler.deduplication;

import com.vv.common.constant.CommonConstant;
import com.vv.common.domain.TaskInfo;
import com.vv.common.enums.DeduplicationType;
import com.vv.common.utils.EnumUtil;
import com.vv.support.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DeduplicationRuleService {

    public static final String DEDUPLICATION_RULE_KEY = "deduplicationRule";

    @Autowired
    private DeduplicationHolder deduplicationHolder;

    @Autowired
    private ConfigService config;

    public void duplication(TaskInfo taskInfo) {
        // 配置样例：{"deduplication_10":{"num":1,"time":300},"deduplication_20":{"num":5}}
        String deduplicationConfig = config.getProperty(DEDUPLICATION_RULE_KEY, CommonConstant.EMPTY_JSON_OBJECT);

        // 去重
        List<Integer> deduplicationList = EnumUtil.getCodeList(DeduplicationType.class);

        for (Integer deduplicationType : deduplicationList) {

            DeduplicationParam deduplicationParam = deduplicationHolder
                    .selectBuilder(deduplicationType)
                    .build(deduplicationConfig, taskInfo);

            if (Objects.nonNull(deduplicationParam)) {

                deduplicationHolder.selectService(deduplicationType).deduplication(deduplicationParam);

            }
        }
    }

}
