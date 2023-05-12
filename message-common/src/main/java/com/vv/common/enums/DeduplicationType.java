package com.vv.common.enums;


import com.vv.common.utils.PowerfulEnum;
import lombok.*;

/**
 * 去重类型
 * 本平台只有两种去重
 * 1.一天五次
 * 2.两次间隔在5分钟内
 */
@AllArgsConstructor
@Getter
@ToString
public enum DeduplicationType implements PowerfulEnum {
    /**
     * 相同内容去重
     */
    CONTENT(10, "N分钟相同内容去重"),

    /**
     * 渠道接受消息 频次 去重
     */
    FREQUENCY(20, "一天内N次相同渠道去重"),
    ;
    private final Integer code;
    private final String description;
}
