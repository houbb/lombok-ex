package com.github.houbb.lombok.ex.annotation;

import java.lang.annotation.*;

/**
 * 注解
 * @author binbin.hou
 * @since 0.0.4
 * @see com.github.houbb.lombok.ex.support.tostring.IToString 字符串构建策略，后续添加这个拓展
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface ToString {
}
