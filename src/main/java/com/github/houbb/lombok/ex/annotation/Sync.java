package com.github.houbb.lombok.ex.annotation;

import java.lang.annotation.*;

/**
 * 为方法添加同步等待
 *
 * @author binbin.hou
 * @since 0.0.5
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
@Documented
public @interface Sync {
}
