package com.jianglei.jllog.methodtrace;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author longyi created on 19-6-14
 */
public class MethodTraceInfo implements Parcelable {

    public static final short IN = 0;
    public static final short OUT = 1;
    /**
     * 类名以及hashcode的结合体,类似于MainActivity@13534645423
     */
    private String classNameAndHash;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 方法描述符，主要是用来区分不同方法的重载
     */
    private String desc;

    /**
     * 方法调用当时的时间
     */
    private long time;

    /**
     * 当前方法是进入还是退出,主要是{@link MethodTraceInfo#IN} 和{@link MethodTraceInfo#OUT}
     */
    private short type;

    private String processName;

    public MethodTraceInfo(String classNameAndHash, String methodName, String desc, long time, short type) {
        this.classNameAndHash = classNameAndHash;
        this.methodName = methodName;
        this.time = time;
        this.desc = desc;
        this.type = type;
    }


    protected MethodTraceInfo(Parcel in) {
        classNameAndHash = in.readString();
        methodName = in.readString();
        desc = in.readString();
        time = in.readLong();
        type = (short) in.readInt();
        processName = in.readString();
    }

    public static final Creator<MethodTraceInfo> CREATOR = new Creator<MethodTraceInfo>() {
        @Override
        public MethodTraceInfo createFromParcel(Parcel in) {
            return new MethodTraceInfo(in);
        }

        @Override
        public MethodTraceInfo[] newArray(int size) {
            return new MethodTraceInfo[size];
        }
    };

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
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

    public String getDesc() {
        return desc;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classNameAndHash);
        dest.writeString(methodName);
        dest.writeString(desc);
        dest.writeLong(time);
        dest.writeInt((int) type);
        dest.writeString(processName);
    }
}
