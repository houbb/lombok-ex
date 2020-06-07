package com.github.houbb.lombok.ex.annotation;

import java.lang.annotation.*;

/**
 * 用于抛出异常
 * @author binbin.hou
 * @since 0.0.9
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Documented
public @interface UnsupportedOperation {

    /**
     * 异常信息
     * @return 结果
     * @since 0.0.9
     */
    String message() default "";

}
