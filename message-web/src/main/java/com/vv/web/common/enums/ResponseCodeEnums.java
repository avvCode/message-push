package com.vv.web.common.enums;

import lombok.Getter;

@Getter
public enum ResponseCodeEnums {
    /**
     * 错误
     */
    ERROR_500("500", "服务器未知错误"),
    ERROR_400("400", "错误请求"),

    /**
     * OK：操作成功
     */
    SUCCESS("0", "操作成功"),
    FAIL("-1", "操作失败"),
    ;

    /**
     * 响应状态码
     */
    private final String code;
    /**
     * 响应状态
     */
    private final String msg;

    ResponseCodeEnums(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
