package com.vv.handler.deduplication.limit;

import cn.hutool.core.util.IdUtil;
import com.vv.common.domain.TaskInfo;
import com.vv.handler.deduplication.DeduplicationParam;
import com.vv.handler.deduplication.service.AbstractDeduplicationService;
import com.vv.support.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 基于滑动窗口实现的频次去重
 * 使用redis的zset数据结构，采用Lua脚本保证查询和插入的原子性
 */
@Service(value = "SlideWindowLimitService")
public class SlideWindowLimitService extends AbstractLimitService{

    private static final String LIMIT_TAG = "SW_";

    @Autowired
    private RedisUtils redisUtils;

    /**
     * redis lua脚本
     */
    private DefaultRedisScript<Long> redisScript;

    @PostConstruct
    public void init() {
        //初始化脚本
        redisScript = new DefaultRedisScript();
        //结果
        redisScript.setResultType(Long.class);
        //需要执行的脚本
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("limit.lua")));
    }

    /**
     * @param service  去重器对象
     * @param taskInfo 当前执行任务对象
     * @param param    去重参数
     * @return 返回不符合条件的手机号码
     */
    @Override
    public Set<String> limitFilter(AbstractDeduplicationService service, TaskInfo taskInfo, DeduplicationParam param) {
        //获取发送人群
        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());
        //获取当前时间
        long nowTime = System.currentTimeMillis();

        for (String receiver : taskInfo.getReceiver()) {
            //构建key
            String key = LIMIT_TAG + deduplicationSingleKey(service, taskInfo, receiver);
            //雪花算法生成唯一id
            String scoreValue = String.valueOf(IdUtil.getSnowflake().nextId());
            //当前时间
            String score = String.valueOf(nowTime);

            if (redisUtils.execLimitLua(redisScript, Collections.singletonList(key), String.valueOf(param.getDeduplicationTime() * 1000), score, String.valueOf(param.getCountNum()), scoreValue)) {
                filterReceiver.add(receiver);
            }

        }
        return filterReceiver;
    }
}
