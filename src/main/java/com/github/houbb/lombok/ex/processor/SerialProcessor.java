package com.github.houbb.lombok.ex.processor;

import com.github.houbb.lombok.ex.annotation.Serial;
import com.github.houbb.lombok.ex.metadata.LClass;
import com.github.houbb.lombok.ex.metadata.LField;
import com.github.houbb.lombok.ex.metadata.LObject;
import com.sun.tools.javac.code.Flags;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * {@link com.github.houbb.lombok.ex.annotation.Serial} 对应的解释器
 * @author binbin.hou
 * @since 0.0.1
 */
@SupportedAnnotationTypes("com.github.houbb.lombok.ex.annotation.Serial")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SerialProcessor extends BaseClassProcessor {

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return Serial.class;
    }

    @Override
    protected void handleClass(LClass lClass) {
        // 给此类添加一个接口
        lClass.addInterface(Serializable.class);

        // 创建一个字段
        createSerialVersionUID(lClass);
    }

    /**
     * private static final long serialVersionUID = 1L;
     *
     * @param lClass 类
     * @since 0.0.1
     */
    private void createSerialVersionUID(LClass lClass) {
        // 获取注解对应的值
        Serial serial = lClass.classSymbol().getAnnotation(Serial.class);

        // 构建对象信息
        LObject value = new LObject(processContext).expression(treeMaker.Literal(serial.value()));
        LField lField = LField.newInstance().modifiers(Flags.PRIVATE | Flags.STATIC | Flags.FINAL)
                .type(Long.class).name("serialVersionUID").value(value);
        lClass.insertField(lField);
    }

}
