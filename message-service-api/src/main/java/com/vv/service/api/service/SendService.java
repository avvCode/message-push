package com.vv.service.api.service;

import com.vv.service.api.domain.BatchSendRequest;
import com.vv.service.api.domain.SendRequest;
import com.vv.service.api.domain.SendResponse;

/**
 * 发送接口
 *
 * @author vv
 */
public interface SendService {


    /**
     * 单文案发送接口
     *
     * @param sendRequest
     * @return
     */
    SendResponse send(SendRequest sendRequest);


    /**
     * 多文案发送接口
     *
     * @param batchSendRequest
     * @return
     */
    SendResponse batchSend(BatchSendRequest batchSendRequest);

}
