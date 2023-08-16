package com.vv.web.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.common.constant.MessagePushConstant;
import com.vv.support.domain.MessageTemplate;
import com.vv.support.mapper.MessageTemplateMapper;
import com.vv.web.dto.messagetemplate.MessageTemplateAddRequest;
import com.vv.web.service.MessageTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author vv
 */
@Service
public class MessageTemplateServiceImpl extends ServiceImpl<MessageTemplateMapper, MessageTemplate>
        implements MessageTemplateService {
    @Override
    public MessageTemplate save(MessageTemplateAddRequest messageTemplateAddRequest) {
        MessageTemplate messageTemplate = new MessageTemplate();
        //TODO 如果有用户，此时修改为从当前登录态中获取用户信息
        messageTemplate.setCreator(MessagePushConstant.DEFAULT_CREATOR);
        messageTemplate.setUpdator(MessagePushConstant.DEFAULT_UPDATER);
        messageTemplate.setCreated(Math.toIntExact(DateUtil.currentSeconds()));
        messageTemplate.setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
        BeanUtils.copyProperties(messageTemplateAddRequest,messageTemplate);
        save(messageTemplate);
        return getById(messageTemplate.getId());
    }
}




