package com.github.houbb.lombok.ex.support.log.impl;

import com.github.houbb.lombok.ex.support.log.ILog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * 入参
 * 出参
 * 耗时
 * 慢日志
 * @author binbin.hou
 * @since 0.0.3
 */
public class ConsoleLog implements ILog {

    /**
     * 日志输出入参
     *
     * @param fullClassName   类名全称
     * @param methodName  方法名
     * @param params      参数列表
     * @since 0.0.3
     */
    @Override
    public void logParam(String fullClassName, String methodName, Object... params) {
        String date = currentDateStr();
        String msg = String.format("[INFO] %s [%s]#<%s>(%s)", date, fullClassName, methodName, Arrays.toString(params));
        System.out.println(msg);
    }

    /**
     * 获取当前时间戳
     * @return 时间戳
     * @since 0.0.3
     */
    private static String currentDateStr() {
        Date date = new Date();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(date);
    }

}
