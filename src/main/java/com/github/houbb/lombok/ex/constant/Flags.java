package com.github.houbb.lombok.ex.constant;

/**
 * 访问级别常量
 * @author binbin.hou
 * @since 0.0.7
 */
public final class Flags {

    private Flags(){}

    public static final int PUBLIC = 1;
    public static final int PRIVATE = 2;
    public static final int PROTECTED = 4;
    public static final int STATIC = 8;
    public static final int FINAL = 16;
    public static final int SYNCHRONIZED = 32;
    public static final int VOLATILE = 64;
    public static final int TRANSIENT = 128;
    public static final int NATIVE = 256;
    public static final int INTERFACE = 512;
    public static final int ABSTRACT = 1024;
    public static final int STRICTFP = 2048;
    public static final int SYNTHETIC = 4096;
    public static final int ANNOTATION = 8192;
    public static final int ENUM = 16384;
    public static final int MANDATED = 32768;
    public static final int StandardFlags = 4095;
    public static final int ACC_SUPER = 32;
    public static final int ACC_BRIDGE = 64;
    public static final int ACC_VARARGS = 128;
    public static final int DEPRECATED = 131072;
    public static final int HASINIT = 262144;
    public static final int BLOCK = 1048576;
    public static final int IPROXY = 2097152;
    public static final int NOOUTERTHIS = 4194304;
    public static final int EXISTS = 8388608;
    public static final int COMPOUND = 16777216;
    public static final int CLASS_SEEN = 33554432;
    public static final int SOURCE_SEEN = 67108864;
    public static final int LOCKED = 134217728;
    public static final int UNATTRIBUTED = 268435456;
    public static final int ANONCONSTR = 536870912;
    public static final int ACYCLIC = 1073741824;
    public static final long BRIDGE = 2147483648L;
    public static final long PARAMETER = 8589934592L;
    public static final long VARARGS = 17179869184L;
    public static final long ACYCLIC_ANN = 34359738368L;
    public static final long GENERATEDCONSTR = 68719476736L;
    public static final long HYPOTHETICAL = 137438953472L;
    public static final long PROPRIETARY = 274877906944L;
    public static final long UNION = 549755813888L;
    public static final long OVERRIDE_BRIDGE = 1099511627776L;
    public static final long EFFECTIVELY_FINAL = 2199023255552L;
    public static final long CLASH = 4398046511104L;
    public static final long DEFAULT = 8796093022208L;
    public static final long AUXILIARY = 17592186044416L;
    public static final long NOT_IN_PROFILE = 35184372088832L;
    public static final long BAD_OVERRIDE = 35184372088832L;
    public static final long SIGNATURE_POLYMORPHIC = 70368744177664L;
    public static final long THROWS = 140737488355328L;
    public static final long POTENTIALLY_AMBIGUOUS = 281474976710656L;
    public static final long LAMBDA_METHOD = 562949953421312L;
    public static final long TYPE_TRANSLATED = 1125899906842624L;
    public static final int AccessFlags = 7;
    public static final int LocalClassFlags = 23568;
    public static final int MemberClassFlags = 24087;
    public static final int ClassFlags = 32273;
    public static final int InterfaceVarFlags = 25;
    public static final int VarFlags = 16607;
    public static final int ConstructorFlags = 7;
    public static final int InterfaceMethodFlags = 1025;
    public static final int MethodFlags = 3391;
    public static final long ExtendedStandardFlags = 8796093026303L;
    public static final long ModifierFlags = 8796093025791L;
    public static final long InterfaceMethodMask = 8796093025289L;
    public static final long AnnotationTypeElementMask = 1025L;
    public static final long LocalVarFlags = 8589934608L;
    public static final long ReceiverParamFlags = 8589934592L;

}
