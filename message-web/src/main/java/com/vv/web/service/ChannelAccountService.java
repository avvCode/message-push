package com.vv.web.service;

import com.vv.support.domain.ChannelAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.web.dto.channelaccount.ChannelAccountAddRequest;

/**
* @author vv
*/
public interface ChannelAccountService extends IService<ChannelAccount> {
    /**
     * 添加渠道账号
     * @param channelAccountAddRequest
     * @return
     */
    ChannelAccount save(ChannelAccountAddRequest channelAccountAddRequest);
}
