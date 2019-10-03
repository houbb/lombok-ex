package com.github.houbb.lombok.ex.metadata;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;

/**
 * @author binbin.hou
 * @since 0.0.1
 */
public class LField {

    /**
     * 字段的标识
     */
    private Symbol.VarSymbol varSymbol;

    /**
     * 字段的声明
     */
    private JCTree.JCVariableDecl variableDecl;

    /**
     * 字段修饰符
     */
    private long modifiers;

    /**
     * 字段类型
     */
    private Class type;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段的值
     */
    private LObject value;

    public static LField newInstance() {
        return new LField();
    }

    public long modifiers() {
        return modifiers;
    }

    public LField modifiers(long modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public String typeName() {
        return type.getName();
    }

    public Class type() {
        return type;
    }

    public LField type(Class type) {
        this.type = type;
        return this;
    }

    public String name() {
        return name;
    }

    public LField name(String fieldName) {
        this.name = fieldName;
        return this;
    }

    public LObject value() {
        return value;
    }

    public LField value(LObject value) {
        this.value = value;
        return this;
    }

}
