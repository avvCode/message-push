package com.vv.web.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.common.constant.MessagePushConstant;
import com.vv.web.dto.channelaccount.ChannelAccountAddRequest;
import com.vv.web.service.ChannelAccountService;
import com.vv.support.domain.ChannelAccount;
import com.vv.support.mapper.ChannelAccountMapper;
import org.springframework.stereotype.Service;

/**
 * @author vv
 */
@Service
public class ChannelAccountServiceImpl extends ServiceImpl<ChannelAccountMapper, ChannelAccount>
        implements ChannelAccountService {
    @Override
    public ChannelAccount save(ChannelAccountAddRequest channelAccountAddRequest) {
        ChannelAccount channelAccount = new ChannelAccount();

        String name = channelAccountAddRequest.getName();
        Integer sendChannel = channelAccountAddRequest.getSendChannel();
        String accountConfig = channelAccountAddRequest.getAccountConfig();
        //TODO 此处应当时根据当前用户来选择。这里未实现登录，先按照默认用户来
        channelAccount.setCreator(MessagePushConstant.DEFAULT_CREATOR);
        channelAccount.setName(name);
        channelAccount.setSendChannel(sendChannel);
        channelAccount.setAccountConfig(accountConfig);
        //以时间戳作为日期
        channelAccount.setCreated(Math.toIntExact(DateUtil.currentSeconds()));
        channelAccount.setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
        save(channelAccount);
        return getById(channelAccount.getId());
    }
}




