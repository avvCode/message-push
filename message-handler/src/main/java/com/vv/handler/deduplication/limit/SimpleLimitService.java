package com.vv.handler.deduplication.limit;

import cn.hutool.core.collection.CollUtil;
import com.vv.common.constant.CommonConstant;
import com.vv.common.domain.TaskInfo;
import com.vv.handler.deduplication.DeduplicationParam;
import com.vv.handler.deduplication.service.AbstractDeduplicationService;
import com.vv.support.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 计数去重
 */
@Service(value = "SimpleLimitService")
public class SimpleLimitService extends AbstractLimitService{

    private static final String LIMIT_TAG = "SP_";

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Set<String> limitFilter(AbstractDeduplicationService service, TaskInfo taskInfo, DeduplicationParam param) {
        //过滤掉的人群
        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());
        //尝试从缓存中拿到取数据
        Map<String, String> readyPutRedisReceiver = new HashMap<>(taskInfo.getReceiver().size());
        //redis数据隔离
        //获取所有发送人的业务key
        List<String> keys  = deduplicationAllKey(service, taskInfo)
                .stream()
                .map(key -> LIMIT_TAG + key)
                .collect(Collectors.toList());
        //获取缓存
        HashMap<String, String> inRedisValue = redisUtils.mGet(keys);
        for (String receiver : taskInfo.getReceiver()) {
            //生成业务key
            String key = LIMIT_TAG + deduplicationSingleKey(service,taskInfo,receiver);
            //看看当前发送人有没有在缓存中
            String value = inRedisValue.get(receiver);
            // 符合条件的用户
            if (Objects.nonNull(value) && Integer.parseInt(value) >= param.getCountNum()) {
                //需要过滤
                filterReceiver.add(receiver);
            } else {
                readyPutRedisReceiver.put(receiver, key);
            }
        }
        // 不符合条件的用户：需要更新Redis(无记录添加，有记录则累加次数)
        putInRedis(readyPutRedisReceiver, inRedisValue, param.getDeduplicationTime());
        return filterReceiver;
    }

    /**
     * 存入redis 实现去重
     */
    private void putInRedis(Map<String, String> readyPutRedisReceiver,
                            Map<String, String> inRedisValue, Long deduplicationTime) {

        Map<String, String> keyValues = new HashMap<>(readyPutRedisReceiver.size());

        for (Map.Entry<String, String> entry : readyPutRedisReceiver.entrySet()) {
            String key = entry.getValue();

            if (Objects.nonNull(inRedisValue.get(key))) {
                //redis已存在，数量加一
                keyValues.put(key, String.valueOf(Integer.parseInt(inRedisValue.get(key)) + 1));
            } else {
                //新放入redis
                keyValues.put(key, String.valueOf(CommonConstant.TRUE));
            }
        }
        //批量处理redis命令
        if (CollUtil.isNotEmpty(keyValues)) {
            redisUtils.pipelineSetEx(keyValues, deduplicationTime);
        }
    }
}
