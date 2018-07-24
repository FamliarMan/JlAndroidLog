package com.jianglei.jllog.aidl;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * crash记录信息
 * @author jianglei
 */

public class CrashVo implements Parcelable {
    private long time;
    private String crashInfo;

    public CrashVo(long time){
        this.time = time;
    }
    protected CrashVo(Parcel in) {
        time = in.readLong();
        crashInfo = in.readString();
    }

    public static final Creator<CrashVo> CREATOR = new Creator<CrashVo>() {
        @Override
        public CrashVo createFromParcel(Parcel in) {
            return new CrashVo(in);
        }

        @Override
        public CrashVo[] newArray(int size) {
            return new CrashVo[size];
        }
    };

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCrashInfo() {
        return crashInfo;
    }

    public void setCrashInfo(String crashInfo) {
        this.crashInfo = crashInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(time);
        parcel.writeString(crashInfo);
    }

    @Override
    public String toString() {
        return getCrashInfo();
    }

}
