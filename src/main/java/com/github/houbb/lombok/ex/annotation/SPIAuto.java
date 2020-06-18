package com.github.houbb.lombok.ex.annotation;

import com.github.houbb.spi.annotation.SPI;

import java.lang.annotation.*;

/**
 * 可以放在类上
 *
 * 用于生成 spi 对应的文件信息
 * @author binbin.hou
 * @since 0.1.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
@Documented
public @interface SPIAuto {

    /**
     * 别称
     * @return 别称
     * @since 0.1.0
     */
    String value() default "";

    /**
     * 目标文件夹
     * @return 文件夹
     * @since 0.1.0
     */
    String dir() default "META-INF/services/";

    /**
     * 对应的 spi 注解信息
     * @return spi 注解
     * @since 0.1.0
     */
    Class<? extends Annotation> spi() default SPI.class;

}
