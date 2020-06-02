package com.github.houbb.lombok.ex.processor;

import com.github.houbb.auto.log.annotation.AutoLog;
import com.github.houbb.lombok.ex.metadata.LMethod;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * {@link com.github.houbb.auto.log.annotation.AutoLog} 对应的解释器
 *
 * @author binbin.hou
 * @since 0.0.3
 */
@SupportedAnnotationTypes("com.github.houbb.auto.log.annotation.AutoLog")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AutoLogProcessor extends BaseMethodProcessor {

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return AutoLog.class;
    }

    /**
     * 1. 内部变量
     *
     * <pre>
     *     private static final ILOG CLASS_LOG = new ConsoleLog();
     * </pre>
     *
     * 2. 输出入参
     *
     * 3. 插入当前时间
     *
     * 4. 输出出参
     *
     * 5. 输出耗时
     *
     * @param method 单个方法息
     */
    @Override
    protected void handleMethod(LMethod method) {

    }

}
