package com.github.houbb.lombok.ex.metadata;

import com.github.houbb.lombok.ex.constant.ClassConst;
import com.github.houbb.lombok.ex.model.ProcessContext;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

/**
 * 类信息
 * @author binbin.hou
 * @since 0.0.1
 */
public class LClass extends LCommon {

    /**
     * 类的标识
     */
    private final Symbol.ClassSymbol classSymbol;

    /**
     * 类的声明
     */
    private final JCTree.JCClassDecl classDecl;

    /**
     * 设置类的修饰符
     *
     * @param modifier 修饰符
     * @return 返回当前类
     * @since 0.0.2
     */
    public LClass modifier(long modifier) {
        classDecl.mods.flags = modifier;
        return this;
    }

    /**
     * 获取当前类的修饰符
     * @return 修饰符
     */
    public long modifier() {
        return classDecl.mods.flags;
    }

    public LClass(ProcessContext processContext, Symbol.ClassSymbol classSymbol) {
        super(processContext);
        this.classSymbol = classSymbol;
        classDecl = super.trees.getTree(classSymbol);
    }

    /**
     * 插入字段信息
     * @param field 字段信息
     * @return this
     * @since 0.0.1
     */
    public LClass insertField(final LField field) {
        ListBuffer<JCTree> statements = new ListBuffer<>();

        // 判断当前字段是否已经存在
        if (existsField(field.name())) {
            return this;
        }

        // 导入包
        importPackage(this, field.type());

        // 添加 JCTree
        JCTree.JCVariableDecl variableDecl = treeMaker.VarDef(treeMaker.Modifiers(field.modifiers()),
                names.fromString(field.name()), buildClassIdent(field.type()), field.value().expression());
        statements.append(variableDecl);

        // 循环添加，重新赋值。
        for (JCTree jcTree : classDecl.defs) {
            statements.append(jcTree);
        }
        classDecl.defs = statements.toList();
        return this;
    }

    /**
     * 添加接口
     *
     * @param interfaceClass 接口类
     * @return 返回当前类
     * @since 0.0.1
     */
    public LClass addInterface(Class<?> interfaceClass) {
        // 判断类有没有实现此接口
        if (!existsInterface(interfaceClass)) {
            // 导包（会自动去重）
            importPackage(this, interfaceClass);

            java.util.List<JCTree.JCExpression> implementing = classDecl.implementing;
            ListBuffer<JCTree.JCExpression> statements = new ListBuffer<>();
            for (JCTree.JCExpression impl : implementing) {
                statements.append(impl);
            }

            // 构建 class symbol 单独提取出来。
            JCTree.JCIdent ident = buildClassIdent(interfaceClass);
            statements.append(ident);
            classDecl.implementing = statements.toList();
        }
        return this;
    }

    /**
     * 构建类标识
     * @param clazz 类信息
     * @return 标识
     * @since 0.0.1
     */
    private JCTree.JCIdent buildClassIdent(final Class clazz) {
        // 当前类修饰符+类修饰符
//        EnumSet<Flags.Flag> flags = Flags.asFlagSet(clazz.getModifiers());
        Symbol.ClassSymbol classSymbol = new Symbol.ClassSymbol(Flags.ClassFlags,
                names.fromString(clazz.getSimpleName()),null);
        return treeMaker.Ident(classSymbol);
    }

    /**
     * 判断有没有实现指定的接口
     *
     * @param interfaceClass 接口
     * @return 如果类已经实现了指定接口则返回true，否则返回false
     * @since 0.0.1
     */
    private boolean existsInterface(Class<?> interfaceClass) {
        for (JCTree.JCExpression impl : classDecl.implementing) {
            if (impl.type.toString().equals(interfaceClass.getName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是否存在字段
     *
     * @param fieldName 字段名
     * @return 若存在返回true，否则返回false
     * @since 0.0.1
     */
    private boolean existsField(String fieldName) {
        for (JCTree jcTree : classDecl.defs) {
            if (jcTree.getKind() == Tree.Kind.VARIABLE) {
                JCTree.JCVariableDecl var = (JCTree.JCVariableDecl) jcTree;

                System.out.println(var.sym.flags_field);
                if (fieldName.equals(var.name.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *  获取类修饰符
     * @since 0.0.1
     * @return 修饰符
     */
    public Symbol.ClassSymbol classSymbol() {
        return classSymbol;
    }

    /**
     * 设置无参数构造器
     *
     * @param modifier 访问级别
     * @return 返回当前类
     */
    public LClass addNoArgConstructor(final long modifier) {
        // 遍历类的所有字段和方法
        for (JCTree jcTree : classDecl.defs) {
            // 只处理方法
            if (jcTree instanceof JCTree.JCMethodDecl) {
                JCTree.JCMethodDecl methodDecl = (JCTree.JCMethodDecl) jcTree;
                // 如果是构造方法 并且 没有参数
                if (ClassConst.CONSTRUCTOR_NAME.equals(methodDecl.name.toString()) && methodDecl.params.isEmpty()) {
                    // 把修饰符改为指定访问级别
                    methodDecl.mods.flags = modifier;
                }
            }
        }

        return this;
    }

}
