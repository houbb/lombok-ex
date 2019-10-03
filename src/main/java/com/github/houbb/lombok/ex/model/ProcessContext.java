package com.github.houbb.lombok.ex.model;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;

/**
 * 执行上下文
 * @author binbin.hou
 * @since 0.0.1
 */
public class ProcessContext {

    /**
     * Messager主要是用来在编译期打log用的
     */
    private Messager messager;

    /**
     * JavacTrees提供了待处理的抽象语法树
     */
    private JavacTrees trees;

    /**
     * TreeMaker封装了创建AST节点的一些方法
     */
    private TreeMaker treeMaker;

    /**
     * Names提供了创建标识符的方法
     */
    private Names names;

    public static ProcessContext newInstance() {
        return new ProcessContext();
    }

    public Messager messager() {
        return messager;
    }

    public ProcessContext messager(Messager messager) {
        this.messager = messager;
        return this;
    }

    public JavacTrees trees() {
        return trees;
    }

    public ProcessContext trees(JavacTrees trees) {
        this.trees = trees;
        return this;
    }

    public TreeMaker treeMaker() {
        return treeMaker;
    }

    public ProcessContext treeMaker(TreeMaker treeMaker) {
        this.treeMaker = treeMaker;
        return this;
    }

    public Names names() {
        return names;
    }

    public ProcessContext names(Names names) {
        this.names = names;
        return this;
    }
}
