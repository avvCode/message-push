package com.vv.service.api.impl.service;

import com.vv.service.api.domain.BatchSendRequest;
import com.vv.service.api.domain.SendRequest;
import com.vv.service.api.domain.SendResponse;
import com.vv.service.api.service.SendService;
import org.springframework.stereotype.Service;

@Service
public class SendServiceImpl implements SendService {
    @Override
    public SendResponse send(SendRequest sendRequest) {
        return null;
    }

    @Override
    public SendResponse batchSend(BatchSendRequest batchSendRequest) {
        return null;
    }
}
