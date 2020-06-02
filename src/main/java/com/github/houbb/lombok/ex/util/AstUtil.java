package com.github.houbb.lombok.ex.util;

import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.lombok.ex.constant.LombokExConst;
import com.github.houbb.lombok.ex.model.ProcessContext;
import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.lang.model.element.Element;

/**
 * 这里实际上是抽象语法树
 *
 * <p> project: lombok-ex-AstUtil </p>
 * <p> create on 2020/6/2 21:54 </p>
 *
 * @author binbin.hou
 * @since 0.0.3
 */
public final class AstUtil {

    private AstUtil(){}

    /**
     * 获取包名称
     * @param context 上下文
     * @param element 元素
     * @return 结果
     * @since 0.0.3
     */
    public static String getPackageName(final ProcessContext context,
                                        Element element) {
        final Trees trees = context.trees();
        return ((JCTree.JCClassDecl) trees.getTree(element.getEnclosingElement())).sym.toString();
    }

    /**
     * 获取方法名称
     * @param context 上下文
     * @param element 元素
     * @return 结果
     * @since 0.0.3
     */
    public static JCTree.JCLiteral getMethodName(final ProcessContext context,
                                                 final Element element) {
        final JCTree.JCLiteral[] methodName = {null};
        JCTree tree = context.trees().getTree(element);

        tree.accept(new TreeTranslator() {
            @Override
            public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
                if (methodName[0] == null) {
                    methodName[0] = context.treeMaker().Literal(jcMethodDecl.getName().toString());
                }
                super.visitMethodDef(jcMethodDecl);
            }
        });

        return methodName[0];
    }

    /**
     * 获取参数名称
     * @param context 上下文
     * @param element 元素
     * @return 结果
     * @since 0.0.3
     */
    public static List<JCTree.JCExpression> getParameters(final ProcessContext context,
                                                          final Element element) {
        final List<JCTree.JCExpression>[] params = new List[]{List.nil()};
        JCTree tree = context.trees().getTree(element);

        tree.accept(new TreeTranslator() {
            @Override
            public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
                for (JCTree.JCVariableDecl decl : jcMethodDecl.getParameters()) {
                    params[0] = params[0].append(context.treeMaker().Ident(decl));
                }
                super.visitMethodDef(jcMethodDecl);
            }
        });

        return params[0];
    }

    /**
     * 导入包信息
     * @param context 上下文
     * @param element 元素信息
     * @param fullClassName 类信息全称
     * @since 0.0.3
     */
    public static void importPackage(final ProcessContext context,
                                     final Element element,
                                     final String fullClassName) {
        JCTree.JCCompilationUnit compilationUnit = (JCTree.JCCompilationUnit) context.trees()
                .getPath(element.getEnclosingElement())
                .getCompilationUnit();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String packageName = fullClassName.substring(0, fullClassName.lastIndexOf("."));

        final TreeMaker treeMaker = context.treeMaker();
        final Names names = context.names();
        JCTree.JCFieldAccess fieldAccess = treeMaker.Select(treeMaker.Ident(names.fromString(packageName)),
                names.fromString(className));
        JCTree.JCImport jcImport = treeMaker.Import(fieldAccess, false);

        ListBuffer<JCTree> imports = new ListBuffer<>();
        imports.append(jcImport);

        for (int i = 0; i < compilationUnit.defs.size(); i++) {
            imports.append(compilationUnit.defs.get(i));
        }

        compilationUnit.defs = imports.toList();
    }

    /**
     * 定义一个变量
     * @param context 上下文
     * @param element 元素
     * @param className 类名称
     * @param varName 变量名称
     * @param args 参数
     * @since 0.0.3
     */
    public static void defineVariable(final ProcessContext context,
                                      final Element element,
                                      final String className,
                                      final String varName,
                                      final List<JCTree.JCExpression> args) {
        final TreeMaker treeMaker = context.treeMaker();
        final Names names = context.names();
        final Trees trees = context.trees();

        JCTree tree = (JCTree) trees.getTree(element.getEnclosingElement());
        tree.accept(new TreeTranslator() {
            @Override
            public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {

                boolean hasVariable = hasVariable(translate(jcClassDecl.defs), className, varName);

                ListBuffer<JCTree> statements = new ListBuffer<>();
                for (JCTree jcTree : jcClassDecl.defs) {
                    statements.append(jcTree);
                }

                if (!hasVariable) {
                    JCTree.JCExpression typeExpr = treeMaker.Ident(names.fromString(className));
                    JCTree.JCNewClass newClassExpr = treeMaker.NewClass(null,
                            List.<JCTree.JCExpression>nil(), typeExpr, args, null);

                    // not inner class, variable is static
                    int modifiers = Flags.PRIVATE;
                    if (jcClassDecl.sym != null) {
                        if (jcClassDecl.sym.flatname.toString().equals(jcClassDecl.sym.fullname.toString())) {
                            modifiers = modifiers | Flags.STATIC;
                        }

                        JCTree.JCVariableDecl variableDecl = treeMaker.VarDef(treeMaker.Modifiers(modifiers),
                                names.fromString(varName), typeExpr, newClassExpr);
                        statements.append(variableDecl);
                        jcClassDecl.defs = statements.toList();
                    }
                }

                super.visitClassDef(jcClassDecl);
            }
        });
    }

    /**
     * 是否拥有这个变量
     * @param oldList 原始列表
     * @param className 类信息
     * @param varName 变量名
     * @return 是否
     * @since 0.0.3
     */
    private static boolean hasVariable(List<JCTree> oldList, String className, String varName) {
        boolean hasField = false;

        for (JCTree jcTree : oldList) {
            if (jcTree.getKind() == Tree.Kind.VARIABLE) {
                JCTree.JCVariableDecl variableDecl = (JCTree.JCVariableDecl) jcTree;

                if (varName.equals(variableDecl.name.toString()) && className.equals(variableDecl.vartype.toString())) {
                    hasField = true;
                    break;
                }
            }
        }

        return hasField;
    }



}
