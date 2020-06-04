package com.github.houbb.lombok.ex.annotation;

import com.github.houbb.lombok.ex.constant.ToStringType;

import java.lang.annotation.*;

/**
 * 注解
 * @author binbin.hou
 * @since 0.0.4
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface ToString {

    /**
     * 转换为字符串的实现方式
     * @return 实现方式
     * @since 0.0.6
     */
    ToStringType value() default ToStringType.CONCAT;

}
