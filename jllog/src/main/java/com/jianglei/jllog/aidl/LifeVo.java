package com.jianglei.jllog.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author jianglei  on 7/23/18.
 * 生命周期的记录vo
 */

public class LifeVo implements Parcelable {

    public static String TYPE_ON_CREATE = "onCreate()";
    public static String TYPE_ON_START = "onStart()";
    public static String TYPE_ON_RESUME = "onResume()";
    public static String TYPE_ON_PAUSE = "onPause()";
    public static String TYPE_ON_STOP = "onStop()";
    public static String TYPE_ON_DESTROY = "onDestroy()";
    public static String TYPE_ON_RESTART = "onRestart()";
    public static String TYPE_ON_ATTACH = "onAttach()";
    public static String TYPE_ON_CREATE_VIEW = "onCreateView()";
    public static String TYPE_ON_ACTIVITY_CREATED = "onActivityCreated()";
    public static String TYPE_ON_DESTROY_VIEW = "onDestroyView()";
    public static String TYPE_ON_DETACH = "onDetach()";


    private long time;
    /**
     * 生命周期具体类型
     */
    private String lifeType;

    /**
     * 生命周期的宿主类
     */
    private String hostClass;

    public LifeVo(long time, String lifeType, String hostClass) {
        this.time = time;
        this.lifeType = lifeType;
        this.hostClass = hostClass;
    }

    protected LifeVo(Parcel in) {
        time = in.readLong();
        lifeType = in.readString();
        hostClass = in.readString();
    }

    public static final Creator<LifeVo> CREATOR = new Creator<LifeVo>() {
        @Override
        public LifeVo createFromParcel(Parcel in) {
            return new LifeVo(in);
        }

        @Override
        public LifeVo[] newArray(int size) {
            return new LifeVo[size];
        }
    };

    public String getHostClass() {
        return hostClass;
    }

    public void setHostClass(String hostClass) {
        this.hostClass = hostClass;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLifeType() {
        return lifeType;
    }

    public void setLifeType(String lifeType) {
        this.lifeType = lifeType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(lifeType);
        dest.writeString(hostClass);
    }

    @Override
    public String toString() {
        return hostClass+"  "+lifeType;
    }
}
