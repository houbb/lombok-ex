package com.github.houbb.lombok.ex.processor;

import com.github.houbb.lombok.ex.annotation.Sync;
import com.github.houbb.lombok.ex.metadata.LMethod;
import com.sun.tools.javac.code.Flags;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.lang.annotation.Annotation;

/**
 * {@link com.github.houbb.lombok.ex.annotation.Sync} 对应的解释器
 *
 * ps: 这里也可以拓展为任意指定访问级别，但是这会造成混乱。
 *
 * 所以不建议改变 private 等访问级别。
 *
 * @author binbin.hou
 * @since 0.0.5
 */
@SupportedAnnotationTypes("com.github.houbb.lombok.ex.annotation.Sync")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SyncProcessor extends BaseMethodProcessor {

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return Sync.class;
    }

    /**
     * 1. 添加 synchronized 属性
     *
     * 如果该属性已经存在，则不用继续添加。
     *
     * @param method 单个方法息
     */
    @Override
    protected void handleMethod(LMethod method) {
        method.addModifier(Flags.SYNCHRONIZED);
    }

}
