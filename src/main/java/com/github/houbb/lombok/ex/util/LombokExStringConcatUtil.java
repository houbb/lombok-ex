package com.github.houbb.lombok.ex.util;

import com.github.houbb.heaven.annotation.CommonEager;
import com.github.houbb.heaven.constant.PunctuationConst;
import com.github.houbb.heaven.support.tuple.impl.Pair;
import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.heaven.util.guava.Guavas;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.lang.reflect.ClassTypeUtil;

import java.util.Arrays;
import java.util.List;

/**
 * @author binbin.hou
 * @since 0.0.6
 */
@Deprecated
public class LombokExStringConcatUtil {

    private LombokExStringConcatUtil(){}


    /**
     * 进行字符串的连接
     *
     * <pre>
     *     return "ToStringConcatTest{" +
     *                 "name='" + name + '\'' +
     *                 ", age=" + age +
     *                 ", ints=" + Arrays.toString(ints) +
     *                 '}';
     * </pre>
     * @param className 类名
     * @param fieldPairs 字段列表
     * @return 结果
     * @since 0.0.6
     */
    public static String concat(final String className,
                                final List<Pair<String, Object>> fieldPairs){

        ArgUtil.notEmpty(className, "className");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(className).append("{");

        // 具体细节可以不断优化，初期可以简单些。
        List<String> fieldInfoList = Guavas.newArrayList(fieldPairs.size());
        for(Pair<String, Object> pair : fieldPairs) {
            String fieldName = pair.getValueOne();
            Object fieldValue = pair.getValueTwo();

            String fieldValueStr = objectToConcatString(fieldValue);
            fieldInfoList.add(fieldName+"="+fieldValueStr);
        }
        String fieldInfoStr = StringUtil.join(fieldInfoList, PunctuationConst.COMMA);
        stringBuilder.append(fieldInfoStr);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /**
     * 转换为 toString() 的方法
     * @param object 对象
     * @return 结果
     * @since 0.0.5
     */
    @CommonEager
    private static String objectToConcatString(final Object object) {
        if(null == object) {
            return "null";
        }

        final Class type = object.getClass();
        if(ClassTypeUtil.isArray(type)) {
            // 这里添加各种不同的类型处理
            if(int[].class == type) {
                return Arrays.toString((boolean[]) object);
            } else if(byte[].class == type) {
                return Arrays.toString((boolean[]) object);
            } else if(char[].class == type) {
                return Arrays.toString((char[]) object);
            } else if(boolean[].class == type) {
                return Arrays.toString((boolean[]) object);
            } else if(float[].class == type) {
                return Arrays.toString((float[]) object);
            } else if(double[].class == type) {
                return Arrays.toString((double[]) object);
            } else if(short[].class == type) {
                return Arrays.toString((short[]) object);
            } else if(long[].class == type) {
                return Arrays.toString((long[]) object);
            }

            // 默认是对象数组
            return Arrays.toString((Object[]) object);
        }

        return object.toString();
    }

}
