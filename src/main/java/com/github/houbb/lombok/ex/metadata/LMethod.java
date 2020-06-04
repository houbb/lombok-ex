package com.github.houbb.lombok.ex.metadata;

import com.github.houbb.lombok.ex.model.ProcessContext;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;

/**
 * <pre>
 *  https://www.programcreek.com/java-api-examples/?class=com.sun.tools.javac.tree.JCTree
 * </pre>
 *
 * @author binbin.hou
 * @since 0.0.3
 */
public class LMethod extends LCommon{

    /**
     * 方法的标识
     * @since 0.0.3
     */
    private Symbol.MethodSymbol methodSymbol;

    /**
     * 方法的声明
     */
    private JCTree.JCMethodDecl methodDecl;

    /**
     * 方法所属的类
     */
    private LClass refClass;

    public LMethod(ProcessContext processContext) {
        super(processContext);
    }

    public LMethod(ProcessContext processContext, Symbol.MethodSymbol methodSymbol) {
        super(processContext);
        this.methodSymbol = methodSymbol;
        this.refClass = new LClass(processContext, (Symbol.ClassSymbol) methodSymbol.owner);
        methodDecl = trees.getTree(methodSymbol);
    }

    public Symbol.MethodSymbol methodSymbol() {
        return methodSymbol;
    }

    public JCTree.JCMethodDecl methodDecl() {
        return methodDecl;
    }

    public LClass refClass() {
        return refClass;
    }

    /**
     * 设置访问级别
     * @param modifier 设置访问级别
     * @return this
     * @since 0.0.5
     */
    public LMethod setModifier(final long modifier) {
        this.methodDecl.mods.flags = modifier;
        return this;
    }

    /**
     * 新增访问级别
     *
     * 1. 以前的保持不变
     * 2. 新增指定的访问级别
     * @param modifier 访问级别
     * @return this
     * @since 0.0.5
     */
    public LMethod addModifier(final long modifier) {
        long originalModifier = this.methodDecl.mods.flags;
        long actualModifier = originalModifier | modifier;

        return this.setModifier(actualModifier);
    }

}
