package com.jianglei.jllog.uiblock;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 描述ui阻塞的类
 * @author jianglei on 12/23/18.
 */

public class UiBlockVo implements Parcelable{
    /**
     * ui阻塞详细信息
     */
    private String stackTrace;
    /**
     * 阻塞时长,单位毫秒
     */
    private long blockTime;

    /**
     * 发生的时间
     */
    private long time;

    public UiBlockVo(String stackTrace, long blockTime, long time) {
        this.stackTrace = stackTrace;
        this.blockTime = blockTime;
        this.time = time;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public long getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(long blockTime) {
        this.blockTime = blockTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    protected UiBlockVo(Parcel in) {
        stackTrace = in.readString();
        blockTime = in.readLong();
        time = in.readLong();
    }

    public static final Creator<UiBlockVo> CREATOR = new Creator<UiBlockVo>() {
        @Override
        public UiBlockVo createFromParcel(Parcel in) {
            return new UiBlockVo(in);
        }

        @Override
        public UiBlockVo[] newArray(int size) {
            return new UiBlockVo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(stackTrace);
        dest.writeLong(blockTime);
        dest.writeLong(time);
    }
}
