package com.github.houbb.lombok.ex.processor;

import com.github.houbb.lombok.ex.annotation.Util;
import com.github.houbb.lombok.ex.metadata.LClass;
import com.sun.tools.javac.code.Flags;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.lang.annotation.Annotation;

/**
 * {@link com.github.houbb.lombok.ex.annotation.Util} 对应的解释器
 *
 * @author binbin.hou
 * @since 0.0.2
 */
@SupportedAnnotationTypes("com.github.houbb.lombok.ex.annotation.Util")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class UtilProcessor extends BaseClassProcessor {

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return Util.class;
    }

    @Override
    protected void handleClass(LClass lClass) {
        // 设置当前类为 final
        long originalModifier = lClass.modifiler();
        lClass.modifier(originalModifier | Flags.FINAL);

        // 添加私有无参构造器
        lClass.addNoArgConstructor(Flags.PRIVATE);

        // 设置所有共有方法为 public
        // 这个暂时不做，感觉弊端大于收益
    }

}
