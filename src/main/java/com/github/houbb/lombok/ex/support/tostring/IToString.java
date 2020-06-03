package com.github.houbb.lombok.ex.support.tostring;

/**
 * @author binbin.hou
 * @since 0.0.3
 */
public interface IToString {

    /**
     * 构建字符串
     * @param instance 对象实例
     * @return 结果
     * @since 0.0.4
     */
    String toString(final Object instance);

}
