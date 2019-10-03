package com.github.houbb.lombok.ex.annotation;

import java.lang.annotation.*;

/**
 * （1）设置当前类为 final 类
 * （2）设置当前类的构造器为私有
 * （3）设置所有共有方法为 static
 * 这个暂时可以不实现
 * @author binbin.hou
 * @since 0.0.2
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface Util {
}
