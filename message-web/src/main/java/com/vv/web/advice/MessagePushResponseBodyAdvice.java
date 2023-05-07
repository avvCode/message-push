package com.vv.web.advice;


import com.vv.web.annotation.MessagePushResponseResult;
import com.vv.common.vo.BasicResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 统一返回结构
 */
@ControllerAdvice(basePackages = "com.vv.web.controller")
public class MessagePushResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final String RETURN_CLASS = "BasicResultVO";

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return methodParameter
                .getContainingClass()
                .isAnnotationPresent(MessagePushResponseResult.class)
                || methodParameter.hasMethodAnnotation(MessagePushResponseResult.class);
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter methodParameter, MediaType mediaType, Class aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (Objects.nonNull(data) && Objects.nonNull(data.getClass())) {
            String simpleName = data.getClass().getSimpleName();
            if (RETURN_CLASS.equalsIgnoreCase(simpleName)) {
                return data;
            }
        }
        return BasicResult.success(data);
    }
}