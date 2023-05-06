package com.vv.web.common;

import com.vv.web.common.enums.ResponseCodeEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用返回类
 * @author vv
 */
@Getter
public class BasicResult<T> implements Serializable {
    /**
     * 响应结果
     */
    T data;
    /**
     * 响应信息
     */
    String msg;
    /**
     * 状态码
     */
    String code;


    public BasicResult(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }

    public BasicResult(ResponseCodeEnums responseCodeEnums) {
        this(responseCodeEnums, null);
    }

    public BasicResult(ResponseCodeEnums responseCodeEnums, T data) {
        this(responseCodeEnums, responseCodeEnums.getMsg(), data);
    }


    public BasicResult(ResponseCodeEnums responseCodeEnums, String msg,T data){
        this.data = data;
        this.msg = msg;
        this.code = responseCodeEnums.getCode();
    }

    /**
     * 带数据的成功
     */
    public static <T> BasicResult<T> success(T data){
        return new BasicResult<T>(ResponseCodeEnums.SUCCESS , data);
    }

    /**
     * 不带数据的成功
     */
    public static <T> BasicResult<T> success(){
        return new BasicResult<T>(ResponseCodeEnums.SUCCESS,null);
    }
    /**
     * 自定义消息的成功
     */
    public static <T> BasicResult<T> success(String msg){
        return new BasicResult<T>(ResponseCodeEnums.SUCCESS,msg,null);
    }

    /**
     * 默认失败
     */
    public static <T> BasicResult<T> fail(){
        return new BasicResult<T> (
                ResponseCodeEnums.FAIL,
                ResponseCodeEnums.FAIL.getMsg(),
                null);
    }

    /**
     * 自定义消息的失败
     */
    public static <T> BasicResult<T> fail(String msg){
        return fail(ResponseCodeEnums.FAIL ,msg);
    }

    public static <T> BasicResult<T> fail(ResponseCodeEnums responseCodeEnums){
        return fail(ResponseCodeEnums.FAIL,responseCodeEnums.getMsg());
    }
    /**
     * 自定义错误消息
     * 自定义错误编码
     */
    public static <T> BasicResult<T> fail(ResponseCodeEnums responseCodeEnums ,String msg){
        return new BasicResult<T> (responseCodeEnums ,msg,null);
    }

}
