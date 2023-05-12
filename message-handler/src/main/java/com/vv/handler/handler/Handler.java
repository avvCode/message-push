package com.vv.handler.handler;

import com.vv.common.domain.TaskInfo;
import com.vv.support.domain.MessageTemplate;

/**
 *
 * @author vv
 */
public interface Handler {
    //
    void doHandler(TaskInfo taskInfo);


    //
    void recall(MessageTemplate messageTemplate);
}
