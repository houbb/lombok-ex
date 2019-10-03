package com.github.houbb.lombok.ex.annotation;

import java.lang.annotation.*;

/**
 * （1）继承自 {@link java.io.Serializable}
 * （2）生成 语句
 *
 * <pre>
 *     private static final long serialVersionUID = 1L;
 * </pre>
 * @author binbin.hou
 * @since 0.0.1
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface Serial {

    /**
     * 每次固定生成默认值
     * 序列号建议直接使用固定的默认值，除非有特殊需求。
     * @return 默认为-1L
     */
    long value() default 1L;

}
