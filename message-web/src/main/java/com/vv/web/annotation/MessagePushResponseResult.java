package com.vv.web.annotation;

import java.lang.annotation.*;


/**
 * 统一返回注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessagePushResponseResult {
}
