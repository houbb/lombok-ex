package com.github.houbb.lombok.ex.processor;

import com.alibaba.fastjson.JSON;
import com.github.houbb.lombok.ex.annotation.ToString;
import com.github.houbb.lombok.ex.constant.ClassConst;
import com.github.houbb.lombok.ex.constant.LombokExConst;
import com.github.houbb.lombok.ex.metadata.LClass;
import com.github.houbb.lombok.ex.support.tostring.impl.ToStringFastJson;
import com.sun.source.util.SimpleTreeVisitor;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
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
        lClass.importPackage(lClass, JSON.class);
        lClass.importPackage(lClass, ToStringFastJson.class);

        //2. 插入方法
        if(!lClass.containsMethod(LombokExConst.TO_STRING)) {
            generateToStringMethod(lClass);
        }
    }

    /**
     * 创建一个 toString() 方法
     * @param lClass 类信息
     * @since 0.0.4
     */
    private void generateToStringMethod(LClass lClass) {
        List<JCTree> defList = lClass.classDecl().defs;

        // 新增一个方法
        final JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
        Name name = names.fromString(LombokExConst.TO_STRING);
//        JCTree.JCExpression expression = ;
//        Type type = new Type.ClassType();
        Symbol.MethodSymbol methodSymbol = new Symbol.MethodSymbol(1, name, null, null);

        // 表达式
        JCTree.JCBlock jcBlock = treeMaker.Block(0, List.<JCTree.JCStatement>nil());
        JCTree.JCMethodDecl methodDecl = treeMaker.MethodDef(methodSymbol, jcBlock);

        // 重新赋值
        defList.add(methodDecl);
        lClass.classDecl().defs = defList;
    }

}
