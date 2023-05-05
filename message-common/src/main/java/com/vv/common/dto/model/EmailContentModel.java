package com.vv.common.dto.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *邮件正文
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailContentModel extends ContentModel{
    /**
     * 标题
     */
    private String title;

    /**
     * 内容(可写入HTML)
     */
    private String content;

    /**
     * 邮件附件链接
     */
    private String url;
}
