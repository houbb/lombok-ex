package com.github.houbb.lombok.ex.processor;

import com.github.houbb.auto.log.annotation.AutoLog;
import com.github.houbb.lombok.ex.constant.LombokExConst;
import com.github.houbb.lombok.ex.metadata.LMethod;
import com.github.houbb.lombok.ex.support.log.impl.ConsoleLog;
import com.github.houbb.lombok.ex.util.AstUtil;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

/**
 * {@link com.github.houbb.auto.log.annotation.AutoLog} 对应的解释器
 *
 * @author binbin.hou
 * @since 0.0.3
 */
@SupportedAnnotationTypes("com.github.houbb.auto.log.annotation.AutoLog")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class AutoLogProcessor extends BaseMethodProcessor {

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return AutoLog.class;
    }

    /**
     * 1. 内部变量
     *
     * 包导入
     * <pre>
     *     private static final ILOG CLASS_LOG = new ConsoleLog();
     * </pre>
     *
     * 2. 输出入参
     *
     * 3. 插入当前时间
     *
     * 4. 输出出参
     *
     * 5. 输出耗时
     *
     * @param method 单个方法息
     */
    @Override
    protected void handleMethod(LMethod method) {
        final Element element = method.methodSymbol();
        //1. 包名称
        String fullClassName = ConsoleLog.class.getName();
        AstUtil.importPackage(processContext, element, fullClassName);

        //2. 创建 field private static final LOMBOK_EX_LOG = new ConsoleLog();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        final String fieldName = LombokExConst.UPPER_PREFIX+"LOG";
        // 这里的 args 由于泛型擦除，jdk7 无法转换，
        final List<JCTree.JCLiteral> args = List.of(treeMaker.Literal(AstUtil.getPackageName(processContext, element)));
        AstUtil.defineVariable(processContext, method.methodSymbol(), className, fieldName,
                List.<JCTree.JCExpression>nil());

        //3. 生成代码
        this.generateBlockCode(fullClassName, element, fieldName);
    }

    /**
     * 生成代码
     * @param element 元素
     * @param varName 变量名
     * @since 0.0.3
     */
    private void generateBlockCode(final String fullClassName, final Element element, final String varName) {
        final TreeMaker treeMaker = processContext.treeMaker();
        final Names names = processContext.names();
        final Trees trees = processContext.trees();

        JCTree tree = (JCTree) trees.getTree(element);

        tree.accept(new TreeTranslator() {
            @Override
            public void visitBlock(JCTree.JCBlock tree) {
                ListBuffer<JCTree.JCStatement> statements = new ListBuffer<JCTree.JCStatement>();

                /**
                 * create code: LOG.logParam(methodName, args);
                 */
                JCTree.JCFieldAccess fieldAccess = treeMaker.Select(treeMaker.Ident(names.fromString(varName)),
                        names.fromString("logParam"));
                JCTree.JCLiteral methodName = AstUtil.getMethodName(processContext, element);
                List args = AstUtil.getParameters(processContext, element);

                // 全称的转换
                JCTree.JCLiteral fullClassNameActual = treeMaker.Literal(fullClassName);
                List params = List.of(fullClassNameActual, methodName);
                for (Object arg : args) {
                    params = params.append(arg);
                }
                JCTree.JCMethodInvocation methodInvocation = treeMaker.Apply(List.<JCTree.JCExpression>nil(),
                        fieldAccess, params);
                JCTree.JCExpressionStatement code = treeMaker.Exec(methodInvocation);
                statements.append(code);

                // 剩下的语句
                for (int i = 0; i < tree.getStatements().size(); i++) {
                    JCTree.JCStatement statement = tree.getStatements().get(i);

                    statements.append(statement);

                    // 最后的时候可以插入出参或者耗时
                    // 不过与很多场景需要考虑。

                }

                result = treeMaker.Block(0, statements.toList());
            }
        });
    }

}
