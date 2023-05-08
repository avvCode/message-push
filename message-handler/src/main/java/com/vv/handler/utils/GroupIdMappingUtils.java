package com.vv.handler.utils;

import com.vv.common.domain.TaskInfo;
import com.vv.common.enums.ChannelType;
import com.vv.common.enums.MessageType;
import com.vv.common.utils.EnumUtil;

import java.util.ArrayList;
import java.util.List;

public class GroupIdMappingUtils {
    /**
    * 获取所有渠道ID
    */
    public static List<String> getAllGroupIds(){
        List<String> groupIds = new ArrayList<>();
        //通过对枚举类增强，获取其中定义的所有value
        // 每一个value都是 EMAIL(40, "email(邮件)", EmailContentModel.class, "email")
        ChannelType[] channelTypes = ChannelType.values();
        //每一个value都是 NOTICE(10, "通知类消息", "notice")这样
        MessageType[] messageTypes = MessageType.values();
        //将他们组合起来
        for (ChannelType channelType : channelTypes) {
            for (MessageType messageType : messageTypes) {
                groupIds.add(channelType.getCodeEn() + "." + messageType.getCodeEn());
            }
        }
        return groupIds;
    }

    /**
     * 根据taskInfo获取groupId
     * @param taskInfo
     * @return
     */
    public static String getGroupIdByTaskInfo(TaskInfo taskInfo) {
        String channelCodeEn = EnumUtil
                .getEnumByCode(taskInfo.getSendChannel(),
                        ChannelType.class).getCodeEn();

        String msgCodeEn = EnumUtil
                .getEnumByCode(taskInfo.getMsgType(),
                        MessageType.class).getCodeEn();

        return channelCodeEn + "." + msgCodeEn;
    }
}
