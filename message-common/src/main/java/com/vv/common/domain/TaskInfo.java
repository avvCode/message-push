package com.vv.common.domain;

import com.vv.common.dto.model.ContentModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 发送任务信息
 *
 * @author vv
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfo {

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 业务Id(数据追踪使用)
     * 生成逻辑参考 TaskInfoUtils
     */
    private Long businessId;
    /**
     * 模板类型
     */
    private Integer templateType;
    /**
     * 接收者，接收者可能有多个，所以使用set集合
     */
    private Set<String> receiver;

    /**
     * 发送的Id类型
     */
    private Integer idType;

    /**
     * 发送渠道
     */
    private Integer sendChannel;


    /**
     * 消息类型
     */
    private Integer msgType;

    /**
     * 发送文案模型
     * message_template表存储的content是JSON(所有内容都会塞进去)
     * 不同的渠道要发送的内容不一样(比如发push会有img，而短信没有)
     * 所以会有ContentModel
     */
    private ContentModel contentModel;

    /**
     * 发送账号（邮件下可有多个发送账号、短信可有多个发送账号..）
     */
    private Long sendAccount;


}
