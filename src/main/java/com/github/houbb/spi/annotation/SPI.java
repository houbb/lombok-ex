package com.github.houbb.spi.annotation;

import java.lang.annotation.*;

/**
 * SPI 注解定义
 * @author binbin.hou
 * @since 0.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface SPI {
}
