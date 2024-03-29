package com.vv.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




/**
 * 消息模板管理 请求参数
 *
 * @author vv
 * @date 2022/1/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplateParam {

    /**
     * 当前页码
     */
    private Integer page = 1;

    /**
     * 当前页大小
     */
    private Integer perPage = 10;

    /**
     * 模板ID
     */
    private Long id;

    /**
     * 当前用户
     */
    private String creator;

    /**
     * 消息接收者(测试发送时使用)
     */
    private String receiver;

    /**
     * 下发参数信息
     */
    private String msgContent;

    /**
     * 模版名称
     */
    private String keywords;
}
