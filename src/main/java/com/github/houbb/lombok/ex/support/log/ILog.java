package com.github.houbb.lombok.ex.support.log;

/**
 * 入参
 * 出参
 * 耗时
 * 慢日志
 * @author binbin.hou
 * @since 0.0.3
 */
public interface ILog {

    /**
     * 日志输出入参
     * @param fullClassName 包名
     * @param methodName 方法名
     * @param params 参数列表
     * @since 0.0.3
     */
    void logParam(String fullClassName, String methodName, Object... params);

}
