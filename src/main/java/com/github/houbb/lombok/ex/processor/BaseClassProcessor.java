package com.github.houbb.lombok.ex.processor;

import com.github.houbb.lombok.ex.metadata.LClass;
import com.sun.tools.javac.code.Symbol;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 抽象注解类执行器
 *
 * @author binbin.hou
 * @since 0.0.2
 */
public abstract class BaseClassProcessor extends BaseProcessor {

    /**
     * 获取抽象的注解类型
     *
     * @return 注解类型
     * @since 0.0.2
     */
    protected abstract Class<? extends Annotation> getAnnotationClass();

    /**
     * 处理单个类信息
     *
     * @param lClass 单个类信息
     * @since 0.0.2
     */
    protected void handleClass(final LClass lClass) {
        // do nothing;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<LClass> classList = getClassList(roundEnv, getAnnotationClass());

        for (LClass lClass : classList) {
            handleClass(lClass);
        }
        return true;
    }

    /**
     * 获取对应的 class 信息列表
     *
     * @param roundEnv 环境信息
     * @param clazz    注解类型
     * @return 列表
     * @since 0.0.2
     */
    protected List<LClass> getClassList(final RoundEnvironment roundEnv,
                                      final Class<? extends Annotation> clazz) {
        List<LClass> classList = new ArrayList<LClass>();
        Set<? extends Element> serialSet = roundEnv.getElementsAnnotatedWith(clazz);

        // 对于每一个类可以分开，使用线程进行处理。
        for (Element element : serialSet) {
            if (element instanceof Symbol.ClassSymbol) {
                LClass lClass = new LClass(processContext, (Symbol.ClassSymbol) element);
                classList.add(lClass);
            }
        }

        return classList;
    }

}
