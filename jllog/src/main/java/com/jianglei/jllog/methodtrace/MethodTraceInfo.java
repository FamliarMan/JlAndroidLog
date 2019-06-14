package com.jianglei.jllog.methodtrace;

/**
 * @author longyi created on 19-6-14
 */
public class MethodTraceInfo {

    public static final short IN = 0;
    public static final short OUT = 1;
    /**
     * 类名以及hashcode的结合体,类似于MainActivity@13534645423
     */
    private String classNameAndHash;

    /**
     * 方法名称
     */
    private String  methodName;

    /**
     * 方法调用当时的时间
     */
    private long time;

    /**
     * 当前方法是进入还是退出,主要是{@link MethodTraceInfo#IN} 和{@link MethodTraceInfo#OUT}
     */
    private short type;

    public MethodTraceInfo(String classNameAndHash, String methodName, long time, short type) {
        this.classNameAndHash = classNameAndHash;
        this.methodName = methodName;
        this.time = time;
        this.type = type;
    }

    public String getClassNameAndHash() {
        return classNameAndHash;
    }

    public void setClassNameAndHash(String classNameAndHash) {
        this.classNameAndHash = classNameAndHash;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }
}
