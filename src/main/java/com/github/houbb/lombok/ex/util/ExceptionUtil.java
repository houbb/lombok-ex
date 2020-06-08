package com.github.houbb.lombok.ex.util;

import com.github.houbb.heaven.util.lang.reflect.ClassUtil;

/**
 * <p> project: lombok-ex-ReflectUtil </p>
 * <p> create on 2020/6/6 19:58 </p>
 *
 * @author binbin.hou
 * @since 0.0.9
 */
public final class ExceptionUtil {

    private ExceptionUtil(){}

    /**
     * 抛出异常
     *
     * @param clazz 异常类
     * @since 0.0.9
     */
    public static void throwException(Class<? extends RuntimeException> clazz) {
        throw ClassUtil.newInstance(clazz);
    }

}
