package com.vv.handler.deduplication.build;

import com.alibaba.fastjson.JSONObject;
import com.vv.common.domain.TaskInfo;
import com.vv.handler.deduplication.DeduplicationHolder;
import com.vv.handler.deduplication.DeduplicationParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * 去重参数构建
 */
public abstract class AbstractDeduplicationBuilder implements Builder{
    /**
     * 去重类型
     */
    protected Integer deduplicationType;

    @Autowired
    private DeduplicationHolder deduplicationHolder;

    @PostConstruct
    public void init(){
        deduplicationHolder.putBuilder(deduplicationType,this);
    }

    public DeduplicationParam getDeduplicationParam(Integer key, String duplicationConfig, TaskInfo taskInfo){
        //将去重配置转换为对象
        JSONObject jsonObject = JSONObject.parseObject(duplicationConfig);
        if (Objects.isNull(jsonObject)) {
            return null;
        }
        DeduplicationParam deduplicationParam = JSONObject.parseObject(jsonObject.getString(DEDUPLICATION_CONFIG_PRE + key), DeduplicationParam.class);
        if (Objects.isNull(deduplicationParam)) {
            return null;
        }
        deduplicationParam.setTaskInfo(taskInfo);
        return deduplicationParam;
    }
}
