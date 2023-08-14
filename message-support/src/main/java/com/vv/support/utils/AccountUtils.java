package com.vv.support.utils;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.vv.support.domain.ChannelAccount;
import com.vv.support.mapper.ChannelAccountMapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 获取账号配置
 */
@Configuration
@Slf4j
public class AccountUtils {
    @Resource
    private ChannelAccountMapper channelAccountMapper;

    public <T> T getChannelAccountById(Integer sendAccountId , Class<T> clazz){
        try {
            ChannelAccount channelAccount = channelAccountMapper.selectById(Long.valueOf(sendAccountId));
            if (channelAccount != null) {
                return JSON.parseObject(channelAccount.getAccountConfig(), clazz);
            }
        } catch (Exception e) {
            log.error("AccountUtils#getAccount fail! e:{}", Throwables.getStackTraceAsString(e));
        }
        return null;
    }

}
