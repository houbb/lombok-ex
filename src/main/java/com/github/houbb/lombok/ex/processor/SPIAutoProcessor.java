package com.github.houbb.lombok.ex.processor;

import com.github.houbb.heaven.support.tuple.impl.Pair;
import com.github.houbb.heaven.util.io.StreamUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.lombok.ex.annotation.SPIAuto;
import com.github.houbb.lombok.ex.exception.LombokExException;
import com.github.houbb.lombok.ex.metadata.LClass;
import com.github.houbb.spi.annotation.SPI;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * {@link com.github.houbb.lombok.ex.annotation.SPIAuto} 对应的解释器
 *
 * 参考：
 * https://github.com/google/auto/blob/master/service/processor/src/main/java/com/google/auto/service/processor/AutoServiceProcessor.java
 *
 * @author binbin.hou
 * @since 0.1.0
 */
@SupportedAnnotationTypes("com.github.houbb.lombok.ex.annotation.SPIAuto")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class SPIAutoProcessor extends BaseClassProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        java.util.List<LClass> classList = super.getClassList(roundEnv, getAnnotationClass());
        Map<String, Set<String>> spiClassMap = new HashMap<>();

        for (LClass lClass : classList) {
            String spiClassName = getSpiClassName(lClass);

            String fullName = lClass.classSymbol().fullname.toString();
            if(StringUtil.isEmpty(spiClassName)) {
                throw new LombokExException("@SPI class not found for class: "
                        + fullName);
            }
            Pair<String, String> aliasAndDirPair = getAliasAndDir(lClass);
            String newLine = aliasAndDirPair.getValueOne()+"="+fullName;

            // 完整的路径：文件夹+接口名
            String filePath = aliasAndDirPair.getValueTwo()+spiClassName;

            Set<String> lineSet = spiClassMap.get(filePath);
            if(lineSet == null) {
                lineSet = new HashSet<>();
            }
            lineSet.add(newLine);
            spiClassMap.put(filePath, lineSet);
        }

        // 生成文件
        generateNewFiles(spiClassMap);

        return true;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return SPIAuto.class;
    }

    /**
     * 获取类的别称
     * @param lClass 类
     * @return 别称
     * @since 0.1.0
     */
    private Pair<String, String> getAliasAndDir(LClass lClass) {
        // 获取注解对应的值
        SPIAuto auto = lClass.classSymbol().getAnnotation(SPIAuto.class);

        //1. 别称
        String fullClassName = lClass.classSymbol().fullname.toString();
        String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf("."));
        String alias = auto.value();
        if(StringUtil.isEmpty(alias)) {
            alias = StringUtil.firstToLowerCase(simpleClassName);
        }

        return Pair.of(alias, auto.dir());
    }

    /**
     * 创建新的文件
     * key: 文件路径
     * value: 对应的内容信息
     * @param spiClassMap 目标文件路径
     * @since 0.1.0
     */
    private void generateNewFiles(Map<String, Set<String>> spiClassMap) {
        Filer filer = processingEnv.getFiler();

        for(Map.Entry<String, Set<String>> entry : spiClassMap.entrySet()) {
            String fullFilePath = entry.getKey();
            Set<String> newLines = entry.getValue();
            try {
                // would like to be able to print the full path
                // before we attempt to get the resource in case the behavior
                // of filer.getResource does change to match the spec, but there's
                // no good way to resolve CLASS_OUTPUT without first getting a resource.
                FileObject existingFile = filer.getResource(StandardLocation.CLASS_OUTPUT, "",fullFilePath);
                System.out.println("Looking for existing resource file at " + existingFile.toUri());
                Set<String> oldLines = new HashSet<>(StreamUtil.readAllLines(existingFile.openInputStream()));
                System.out.println("Looking for existing resource file set " + oldLines);

                // 写入
                newLines.addAll(oldLines);
                StreamUtil.write(newLines, existingFile.openOutputStream());
                return;
            } catch (IOException e) {
                // According to the javadoc, Filer.getResource throws an exception
                // if the file doesn't already exist.  In practice this doesn't
                // appear to be the case.  Filer.getResource will happily return a
                // FileObject that refers to a non-existent file but will throw
                // IOException if you try to open an input stream for it.

                // 文件不存在的情况下
                System.out.println("Resources file not exists.");
            }

            try {
                FileObject newFile = filer.createResource(StandardLocation.CLASS_OUTPUT, "",
                        fullFilePath);
                try(OutputStream outputStream = newFile.openOutputStream();) {
                    StreamUtil.write(newLines, outputStream);
                    System.out.println("Write into file "+newFile.toUri());
                } catch (IOException e) {
                    throw new LombokExException(e);
                }
            } catch (IOException e) {
                throw new LombokExException(e);
            }
        }
    }

    /**
     * 获取对应的 spi 类
     * @param lClass 类信息
     * @return 结果
     * @since 0.1.0
     */
    private String getSpiClassName(final LClass lClass) {
        List<Type> typeList =  lClass.classSymbol().getInterfaces();
        if(null == typeList || typeList.isEmpty()) {
            return "";
        }

        // 获取注解对应的值
        SPIAuto auto = lClass.classSymbol().getAnnotation(SPIAuto.class);

        for(Type type : typeList) {
            Symbol.ClassSymbol tsym = (Symbol.ClassSymbol) type.tsym;

            //TOOD: 后期这里添加一下拓展。
            if(tsym.getAnnotation(SPI.class) != null) {
                return tsym.fullname.toString();
            }
        }

        return "";
    }

}
