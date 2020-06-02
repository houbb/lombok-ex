package com.github.houbb.lombok.ex.processor;

import com.github.houbb.lombok.ex.metadata.LMethod;
import com.sun.tools.javac.code.Symbol;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 抽象注解方法执行器
 *
 * @author binbin.hou
 * @since 0.0.3
 */
public abstract class BaseMethodProcessor extends BaseProcessor {

    /**
     * 获取抽象的注解类型
     *
     * @return 注解类型
     * @since 0.0.3
     */
    protected abstract Class<? extends Annotation> getAnnotationClass();

    /**
     * 处理单个类信息
     *
     * @param method 单个方法息
     * @since 0.0.3
     */
    protected abstract void handleMethod(final LMethod method);

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<LMethod> methodList = getMethodList(roundEnv, getAnnotationClass());

        for (LMethod method : methodList) {
            handleMethod(method);
        }
        return true;
    }

    /**
     * 获取带有指定注解的方法
     *
     * @param roundEnv 环境信息
     * @param annotationClass 注解类
     * @return 返回所有带有指定注解的方法
     * @since 0.0.3
     */
    protected List<LMethod> getMethodList(final RoundEnvironment roundEnv,
                                          Class<? extends Annotation> annotationClass) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotationClass);
        List<LMethod> methodList = new ArrayList<>(elements.size());
        for (Element e : elements) {
            // 方法信息
            if (e.getKind() == ElementKind.METHOD
                && e instanceof Symbol.MethodSymbol) {
                methodList.add(new LMethod(processContext, (Symbol.MethodSymbol) e));
            }
        }
        return methodList;
    }

}
