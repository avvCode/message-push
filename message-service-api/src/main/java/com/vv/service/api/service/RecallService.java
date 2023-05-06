package com.vv.service.api.service;

import com.vv.service.api.domain.SendRequest;
import com.vv.service.api.domain.SendResponse;

/**
 * 撤回接口
 *
 * @author vv
 */
public interface RecallService {


    /**
     * 根据模板ID撤回消息
     *
     * @param sendRequest
     * @return
     */
    SendResponse recall(SendRequest sendRequest);
}
