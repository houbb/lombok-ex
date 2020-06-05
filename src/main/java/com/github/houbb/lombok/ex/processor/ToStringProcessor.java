package com.github.houbb.lombok.ex.processor;

import com.alibaba.fastjson.JSON;
import com.github.houbb.lombok.ex.annotation.ToString;
import com.github.houbb.lombok.ex.constant.LombokExConst;
import com.github.houbb.lombok.ex.constant.ToStringType;
import com.github.houbb.lombok.ex.metadata.LClass;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.lang.annotation.Annotation;
import java.util.Arrays;

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
        ListBuffer<JCTree> defBufferList = new ListBuffer<JCTree>();
        List<JCTree> defList = lClass.classDecl().defs;
        // 添加旧方法
        defBufferList.addAll(defList);

        // 新增一个方法
        final JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
        Name name = names.fromString(LombokExConst.TO_STRING);

        // 表达式
        // 这里缺少了 @Override 注解
        List<JCTree.JCStatement> statements = createToStringStatements(lClass);

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
     * 构建 toString() 语句
     * @param lClass 类
     * @return 结果
     * @since 0.0.1
     */
    @SuppressWarnings("unchecked")
    private  List<JCTree.JCStatement> createToStringStatements(final LClass lClass) {
        // 获取注解对应的值
        ToString toString = lClass.classSymbol().getAnnotation(ToString.class);
        ToStringType type = toString.value();

        if(ToStringType.FAST_JSON.equals(type)) {
            return createFastJsonStatements(lClass);
        }

        // 基于字符串拼接的实现
        return createFastJsonStatements(lClass);
//        return createStringConcatStatements(lClass);
    }

    /**
     * 创建
     *
     * <pre>
     *     return JSON.toJSONString(this)
     * </pre>
     *
     * 待完善的地方：
     * JSON 可能会有重名，后期可以拓展一下。
     * @return 0.0.4
     */
    private List<JCTree.JCStatement> createFastJsonStatements(final LClass lClass) {
        //1. 导包
        lClass.importPackage(lClass, JSON.class);

        //2. 构建 statement
        JCTree.JCFieldAccess fieldAccess = treeMaker.Select(treeMaker.Ident(names.fromString("JSON")),
                names.fromString("toJSONString"));
        // 避免类型擦除
        ListBuffer<JCTree.JCExpression> identBuffers = new ListBuffer<JCTree.JCExpression>();
        identBuffers.add(treeMaker.Ident(names.fromString("this")));
        JCTree.JCMethodInvocation methodInvocation = treeMaker.Apply(List.<JCTree.JCExpression>nil(),
fieldAccess, identBuffers.toList());

        JCTree.JCStatement statement = treeMaker.Return(methodInvocation);
        return List.of(statement);
    }

    /**
     * 创建
     *
     * 遍历字段，拼接。
     *
     * <pre>
     *
     * </pre>
     *
     * 待完善的地方：
     *
     * TODO: 这里因为 jdk tools 版本不同，导致包冲突。
     *
     * 暂时不做处理。
     * @return 0.0.6
     */
//    private List<JCTree.JCStatement> createStringConcatStatements(final LClass lClass) {
//        String fullClassName = lClass.classSymbol().fullname.toString();
//        String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
//
//        //2. 构建 statement
//        // 所有的字符串都是一个 Literal
//        JCTree.JCLiteral start = treeMaker.Literal(className+"{");
//        // 输出字段信息
//        JCTree.JCBinary lhs = null;
//
//        // TODO: 这里不同的 JDK 版本不兼容
//        // 暂时使用这个版本
//        final JCTree.Tag tag = JCTree.Tag.PLUS;
//        for(JCTree jcTree : lClass.classDecl().defs) {
//            if(jcTree.getKind() == Tree.Kind.VARIABLE) {
//                JCTree.JCVariableDecl variableDecl = (JCTree.JCVariableDecl) jcTree;
//                String varName = variableDecl.name.toString();
//
//                // 初次加載
//                if(lhs == null) {
//                    JCTree.JCLiteral fieldName = treeMaker.Literal(varName+"=");
//                    lhs = treeMaker.Binary(tag, start, fieldName);
//                } else {
//                    JCTree.JCLiteral fieldName = treeMaker.Literal(", "+varName+"=");
//                    lhs = treeMaker.Binary(tag, lhs, fieldName);
//                }
//
//                // 类型为 String 可以考虑加单引号，但是没必要。，判断逻辑比较麻烦
//                String typeName = variableDecl.vartype.toString();
//                if(typeName.endsWith("[]")) {
//                    JCTree.JCMethodInvocation methodInvocation = buildArraysToString(lClass, varName);
//                    lhs = treeMaker.Binary(tag, lhs, methodInvocation);
//                } else {
//                    // 默认直接使用字符串
//                    JCTree.JCIdent fieldValue = treeMaker.Ident(names.fromString(varName));
//                    lhs = treeMaker.Binary(tag, lhs, fieldValue);
//                }
//            }
//        }
//
//        JCTree.JCLiteral rhs = treeMaker.Literal("}");
//        JCTree.JCBinary binary = treeMaker.Binary(tag, lhs, rhs);
//        JCTree.JCStatement statement = treeMaker.Return(binary);
//        return List.of(statement);
//    }

    /**
     * 构建数组调用
     *
     * <pre>
     *     Arrays.toString("xxx");
     * </pre>
     * @param lClass 类
     * @param varName 命名
     * @return 结果
     * @since 0.0.6
     */
    private JCTree.JCMethodInvocation buildArraysToString(final LClass lClass,
                      final String varName) {
        lClass.importPackage(lClass, Arrays.class);

        //2. 构建 statement
        JCTree.JCFieldAccess fieldAccess = treeMaker.Select(treeMaker.Ident(names.fromString("Arrays")), names.fromString("toString"));
        // 避免类型擦除
        ListBuffer<JCTree.JCExpression> identBuffers = new ListBuffer<JCTree.JCExpression>();
        identBuffers.add(treeMaker.Ident(names.fromString(varName)));

        return treeMaker.Apply(List.<JCTree.JCExpression>nil(),
                fieldAccess, identBuffers.toList());
    }

}
