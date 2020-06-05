package com.github.houbb.lombok.ex.annotation;

import java.lang.annotation.*;

/**
 * 可以放在类，方法，字段上。
 *
 * 仅仅用于修饰对应的属性。
 *
 * 建议：合理使用，不要加一些莫名其妙的东西。
 * @author binbin.hou
 * @since 0.0.7
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface Modifiers {

    /**
     * 对应的修饰符
     * @return 修饰符
     * @since 0.0.7
     */
    long value();

    /**
     * 是否为设置模式
     *
     * 1. 如果设置为 true，则使用 append 模式，则已有的枚举值上新增。
     * 2. 否则直接设置
     * @return 是否
     * @since 0.0.7
     */
    boolean appendMode() default true;

}
