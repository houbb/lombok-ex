package com.github.houbb.lombok.ex.processor;

import com.alibaba.fastjson.JSON;
import com.github.houbb.lombok.ex.annotation.ToString;
import com.github.houbb.lombok.ex.metadata.LClass;
import com.github.houbb.lombok.ex.support.tostring.impl.ToStringFastJson;
import com.github.houbb.lombok.ex.util.AstUtil;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

/**
 *
 * toString() 实现策略
 * @author binbin.hou
 * @since 0.0.4
 */
@SupportedAnnotationTypes("com.github.houbb.lombok.ex.annotation.ToString")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ToStringProcessor extends BaseClassProcessor {

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ToString.class;
    }

    @Override
    protected void handleClass(LClass lClass) {
        //1. 导包
        final Element element = lClass.classSymbol();
        AstUtil.importPackage(processContext, element, JSON.class.getName());
        AstUtil.importPackage(processContext, element, ToStringFastJson.class.getName());

        //2. 遍历方法

    }

    @Override
    public String toString() {
        return "ToStringProcessor{}";
    }

}
