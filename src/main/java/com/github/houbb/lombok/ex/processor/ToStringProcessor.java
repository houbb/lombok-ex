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
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

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
        lClass.importPackage(lClass, JSON.class);
        lClass.importPackage(lClass, ToStringFastJson.class);

        //2. 插入方法
        if(!lClass.containsMethod(LombokExConst.TO_STRING)) {
            generateToStringMethod(lClass);
        }
    }

    /**
     * 创建一个 toString() 方法
     *
     * this.mods = var1;
     * this.name = var2;
     * this.restype = var3;
     * this.typarams = var4;
     * this.params = var6;
     * this.recvparam = var5;  // 这个默认时
     * this.thrown = var7;
     * this.body = var8;
     * this.defaultValue = var9;
     * this.sym = var10;    // 这个为空
     *
     * @param lClass 类信息
     * @since 0.0.4
     */
    private void generateToStringMethod(LClass lClass) {
        ListBuffer<JCTree> defBufferList = new ListBuffer<>();
        List<JCTree> defList = lClass.classDecl().defs;
        // 添加旧方法
        defBufferList.addAll(defList);

        // 新增一个方法
        final JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
        Name name = names.fromString(LombokExConst.TO_STRING);
        // 表达式
        // 这里缺少了 @Override 注解
        List<JCTree.JCStatement> statements = List.of(createJSONString());

        JCTree.JCBlock jcBlock = treeMaker.Block(0, statements);
        JCTree.JCExpression restype = treeMaker.Ident(names.fromString("String"));
        JCTree.JCMethodDecl methodDecl = treeMaker.MethodDef(modifiers,
                name,
                restype,
                List.<JCTree.JCTypeParameter>nil(),
                List.<JCTree.JCVariableDecl>nil(),
                List.<JCTree.JCExpression>nil(),
                jcBlock,
                null);


        // 重新赋值
        defBufferList.add(methodDecl);

        lClass.classDecl().defs = defBufferList.toList();
    }

    /**
     * 创建
     *
     * <pre>
     *     return JSON.toJSONString(this)
     * </pre>
     *
     * 待完善的地方：
     *
     * JSON 可能会有重名，后期可以拓展一下。
     * @return 0.0.4
     */
    private JCTree.JCStatement createJSONString() {
        JCTree.JCFieldAccess fieldAccess = treeMaker.Select(treeMaker.Ident(names.fromString("JSON")),
                names.fromString("toJSONString"));
        // 避免类型擦除
        ListBuffer<JCTree.JCExpression> identBuffers = new ListBuffer<>();
        identBuffers.add(treeMaker.Ident(names.fromString("this")));
        JCTree.JCMethodInvocation methodInvocation = treeMaker.Apply(List.<JCTree.JCExpression>nil(),
fieldAccess, identBuffers.toList());

        return treeMaker.Return(methodInvocation);
    }

}
