package com.vv.web.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vv.support.domain.ChannelAccount;
import com.vv.web.annotation.MessagePushResponseResult;
import com.vv.common.constant.MessagePushConstant;
import com.vv.web.service.ChannelAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 渠道账号相关
 */
@Api(tags = "渠道账号管理")
@RestController
@MessagePushResponseResult
@Slf4j
public class ChannelAccountController {

    @Resource
    private ChannelAccountService channelAccountService;

    /**
     * 保存渠道账号信息 id不存在
     * 更新渠道账号信息 id存在
     */
    @PostMapping("/save")
    @ApiOperation("/保存账号信息")
    public ChannelAccount saveOrUpdate(@RequestBody ChannelAccount channelAccount){
        log.info("{}",channelAccount);
        channelAccount.setCreator(StrUtil.isBlank(channelAccount.getCreator())
                ? MessagePushConstant.DEFAULT_CREATOR : channelAccount.getCreator());

        channelAccountService.saveOrUpdate(channelAccount);
        return channelAccount;
    }
    /**
     * 获取所有渠道账号信息
     */
    @GetMapping("/list")
    @ApiOperation("/根据创建者查询所有渠道信息")
    public List<ChannelAccount> list(String creator){
        QueryWrapper<ChannelAccount> channelAccountQueryWrapper = new QueryWrapper<>();
        channelAccountQueryWrapper.eq("creator",creator);
        List<ChannelAccount> list = channelAccountService.list(channelAccountQueryWrapper);
        return list;
    }
    /**
     * 根据Id删除
     * id多个用逗号分隔开
     */
    @DeleteMapping("delete/{id}")
    @ApiOperation("/根据Ids删除")
    public void deleteByIds(@PathVariable("id") String id) {
        if (StrUtil.isNotBlank(id)) {
            List<Long> idList = Arrays.stream(id.split(StrUtil.COMMA)).map(Long::valueOf).collect(Collectors.toList());
            channelAccountService.removeBatchByIds(idList);
        }
    }
}
