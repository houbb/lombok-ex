package com.github.houbb.lombok.ex.constant;

/**
 * @author binbin.hou
 * @since 0.0.6
 */
public enum ToStringType {

    /**
     * 基于 fastJson 实现
     * @since 0.0.6
     */
    FAST_JSON,

    /**
     * 字符串拼接
     *
     * 这个优于 jdk tools 的版本兼容性问题，暂时效果同 FAST_JSON
     * @since 0.0.6
     */
    @Deprecated
    CONCAT,

    ;
}
