package com.github.houbb.lombok.ex.metadata;

import com.github.houbb.lombok.ex.model.ProcessContext;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;

/**
 * 抽象父类
 * @author binbin.hou
 * @since 0.0.1
 */
public class LCommon {

    /**
     * Messager主要是用来在编译期打log用的
     */
    protected Messager messager;

    /**
     * JavacTrees提供了待处理的抽象语法树
     */
    protected JavacTrees trees;

    /**
     * TreeMaker封装了创建AST节点的一些方法
     */
    protected TreeMaker treeMaker;

    /**
     * Names提供了创建标识符的方法
     */
    protected Names names;

    public LCommon(ProcessContext processContext) {
        this.treeMaker = processContext.treeMaker();
        this.trees = processContext.trees();
        this.names = processContext.names();
        this.messager = processContext.messager();
    }

    /**
     * 导入一个包
     *
     * @param lClass    所在的类
     * @param importClass 要导入的包
     * @since 0.0.1
     */
    public void importPackage(LClass lClass, Class<?> importClass) {
        JCTree.JCCompilationUnit compilationUnit = (JCTree.JCCompilationUnit) trees.getPath(lClass.classSymbol()).getCompilationUnit();
        ListBuffer<JCTree> imports = new ListBuffer<JCTree>();

        JCTree.JCIdent packageIdent = treeMaker.Ident(names.fromString(importClass.getPackage().getName()));
        JCTree.JCFieldAccess fieldAccess = treeMaker.Select(packageIdent,
                names.fromString(importClass.getSimpleName()));
        JCTree.JCImport jcImport = treeMaker.Import(fieldAccess, false);
        imports.append(jcImport);

        for (int i = 0; i < compilationUnit.defs.size(); i++) {
            imports.append(compilationUnit.defs.get(i));
        }

        compilationUnit.defs = imports.toList();
    }


}
