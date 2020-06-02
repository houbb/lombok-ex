//package com.github.houbb.lombok.ex.test;
//
//import com.github.javaparser.StaticJavaParser;
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.expr.BinaryExpr;
//import com.github.javaparser.ast.expr.Expression;
//import com.github.javaparser.printer.XmlPrinter;
//import com.github.javaparser.printer.YamlPrinter;
//import com.github.javaparser.resolution.types.ResolvedType;
//import com.github.javaparser.symbolsolver.JavaSymbolSolver;
//import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
//import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
//
///**
// * @author binbin.hou
// * @since 1.0.0
// */
//public class ShowCodeTest {
//
//        public static void main(String[] args) {
//
//            // Parse the code you want to inspect:
//            CompilationUnit cu = StaticJavaParser.parse("class X { int x; }");
//
//            Expression expression = StaticJavaParser.parseExpression("1+1");
//
//            // Now comes the inspection code:
//            System.out.println(cu);
//
//            // Now comes the inspection code:
////            YamlPrinter printer = new YamlPrinter(true);
////            System.out.println(printer.output(cu));
//
//            // Now comes the inspection code:
//            XmlPrinter printer = new XmlPrinter(true);
//            System.out.println(printer.output(cu));
//        }
//
//}
