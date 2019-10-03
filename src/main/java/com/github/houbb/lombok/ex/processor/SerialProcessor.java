package com.github.houbb.lombok.ex.processor;

import com.github.houbb.lombok.ex.annotation.Serial;
import com.github.houbb.lombok.ex.metadata.LClass;
import com.github.houbb.lombok.ex.metadata.LField;
import com.github.houbb.lombok.ex.metadata.LObject;
import com.github.houbb.lombok.ex.model.ProcessContext;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.Serializable;
import java.util.Set;

/**
 * {@link com.github.houbb.lombok.ex.annotation.Serial} 对应的解释器
 * @author binbin.hou
 * @since 0.0.1
 */
@SupportedAnnotationTypes("com.github.houbb.lombok.ex.annotation.Serial")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class SerialProcessor extends AbstractProcessor {

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

    /**
     * 执行上下文
     */
    private ProcessContext processContext;

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

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> serialSet = roundEnv.getElementsAnnotatedWith(Serial.class);

        // 对于每一个类可以分开，使用线程进行处理。
        for(Element element : serialSet) {
            if (element instanceof Symbol.ClassSymbol) {
                LClass lClass = new LClass(processContext, (Symbol.ClassSymbol) element);
                process(lClass);
                messager.printMessage(Diagnostic.Kind.NOTE, "Serial has been processed");
            }
        }
        return true;
    }

    /**
     * 处理每一个类
     *
     * @param lClass 类
     */
    private void process(LClass lClass) {
        // 给此类添加一个接口
        lClass.addInterface(Serializable.class);

        // 创建一个字段
        createSerialVersionUID(lClass);
    }

    /**
     * private static final long serialVersionUID = 1L;
     *
     * @param lClass 类
     * @since 0.0.1
     */
    private void createSerialVersionUID(LClass lClass) {
        // 获取注解对应的值
        Serial serial = lClass.classSymbol().getAnnotation(Serial.class);

        // 构建对象信息
        LObject value = new LObject(processContext).expression(treeMaker.Literal(serial.value()));
        LField lField = LField.newInstance().modifiers(Flags.PRIVATE | Flags.STATIC | Flags.FINAL)
                .type(Long.class).name("serialVersionUID").value(value);
        lClass.insertField(lField);
    }

}
