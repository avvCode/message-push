package com.vv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.support.domain.MessageTemplate;
import com.vv.web.dto.messagetemplate.MessageTemplateAddRequest;

/**
 * @author vv
 */
public interface MessageTemplateService extends IService<MessageTemplate> {
    MessageTemplate save(MessageTemplateAddRequest messageTemplateAddRequest);
}