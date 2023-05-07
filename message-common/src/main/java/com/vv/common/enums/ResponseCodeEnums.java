package com.vv.common.enums;

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
    /**
     * 客户端
     */
    CLIENT_BAD_PARAMETERS("A0001", "客户端参数错误"),
    TEMPLATE_NOT_FOUND("A0002", "找不到模板或模板已被删除"),
    TOO_MANY_RECEIVER("A0003", "传入的接收者大于100个"),
    /**
     * 系统
     */
    SERVICE_ERROR("B0001", "服务执行异常"),
    RESOURCE_NOT_FOUND("B0404", "资源不存在"),
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
