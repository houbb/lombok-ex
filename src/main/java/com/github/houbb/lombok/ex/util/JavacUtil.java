package com.github.houbb.lombok.ex.util;

import com.sun.tools.javac.code.Source;
import com.sun.tools.javac.main.JavaCompiler;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * javac 编译辅助类
 * <p> project: lombok-ex-JavacUtil </p>
 * <p> create on 2020/6/6 19:33 </p>
 *
 * @author binbin.hou
 * @since 0.0.8
 */
public final class JavacUtil {

    private JavacUtil(){}

    private static final Pattern VERSION_PARSER = Pattern.compile("^(\\d{1,6})\\.?(\\d{1,6})?.*$");
    private static final Pattern SOURCE_PARSER = Pattern.compile("^JDK(\\d{1,6})_?(\\d{1,6})?.*$");

    private static final AtomicInteger COMPILER_VERSION = new AtomicInteger(-1);

    /**
     * Returns the version of this java compiler, i.e. the JDK that it shipped in. For example, for javac v1.7, this returns {@code 7}.
     */
    public static int getJavaCompilerVersion() {
        int cv = COMPILER_VERSION.get();
        if (cv != -1) {
            return cv;
        }

        /* Main algorithm: Use JavaCompiler's intended method to do this */ {
            Matcher m = VERSION_PARSER.matcher(JavaCompiler.version());
            if (m.matches()) {
                int major = Integer.parseInt(m.group(1));
                if (major == 1) {
                    int minor = Integer.parseInt(m.group(2));
                    return setVersion(minor);
                }
                if (major >= 9) {
                    return setVersion(major);
                }
            }
        }

        /* Fallback algorithm one: Check Source's values. Lets hope oracle never releases a javac that recognizes future versions for -source */ {
            String name = Source.values()[Source.values().length - 1].name();
            Matcher m = SOURCE_PARSER.matcher(name);
            if (m.matches()) {
                int major = Integer.parseInt(m.group(1));
                if (major == 1) {
                    int minor = Integer.parseInt(m.group(2));
                    return setVersion(minor);
                }
                if (major >= 9) {
                    return setVersion(major);
                }
            }
        }
        return setVersion(6);
    }

    private static int setVersion(int version) {
        COMPILER_VERSION.set(version);
        return version;
    }

}
