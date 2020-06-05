package com.github.houbb.lombok.ex.processor;

import com.github.houbb.lombok.ex.annotation.Modifiers;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 *
 * 访问级别-实现策略
 * @author binbin.hou
 * @since 0.0.7
 * @see com.github.houbb.lombok.ex.annotation.Modifiers 访问级别
 */
@SupportedAnnotationTypes("com.github.houbb.lombok.ex.annotation.Modifiers")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ModifiersProcessor extends BaseProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> modifiersSet = roundEnv.getElementsAnnotatedWith(Modifiers.class);

        for (Element element : modifiersSet) {
            //1. 类
            Modifiers modifiers = element.getAnnotation(Modifiers.class);
            if (element instanceof Symbol.ClassSymbol) {
                Symbol.ClassSymbol symbol = (Symbol.ClassSymbol) element;
                JCTree.JCClassDecl classDecl = trees.getTree(symbol);

                updateModifiers(classDecl.mods, modifiers);
            } else if(element instanceof Symbol.MethodSymbol) {
                //2. 方法
                Symbol.MethodSymbol symbol = (Symbol.MethodSymbol) element;
                JCTree.JCMethodDecl methodDecl = trees.getTree(symbol);
                updateModifiers(methodDecl.mods, modifiers);
            } else if(element.getKind().isField()) {
                //3. 字段
                JCTree.JCVariableDecl variableDecl = (JCTree.JCVariableDecl) trees.getTree(element);
                updateModifiers(variableDecl.mods, modifiers);
            }
        }

        return true;
    }

    /**
     * 更新访问级别信息
     * @param jcModifiers 访问级别
     * @param modifiers 变量
     * @since 0.0.7
     */
    private void updateModifiers(final JCTree.JCModifiers jcModifiers,
                                 final Modifiers modifiers) {
        final long oldValue = jcModifiers.flags;

        long newValue = modifiers.value();
        boolean isAppendMode = modifiers.appendMode();

        // 使用 appends 模式
        if(isAppendMode) {
            newValue = oldValue | newValue;
        }

        jcModifiers.flags = newValue;
    }

}
