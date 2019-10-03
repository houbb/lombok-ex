package com.github.houbb.lombok.ex.processor;

import com.github.houbb.lombok.ex.metadata.LClass;
import com.github.houbb.lombok.ex.model.ProcessContext;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 抽象执行器
 * @author binbin.hou
 * @since 0.0.2
 */
public abstract class BaseProcessor extends AbstractProcessor {

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

    /**
     * 执行上下文
     */
    protected ProcessContext processContext;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);

        this.processContext = ProcessContext.newInstance().messager(messager)
                .names(names).treeMaker(treeMaker).trees(trees);
    }

}
