package com.jianglei.jllog.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author jianglei on 11/22/18.
 */

public class TransformData implements Parcelable{
    public static final int TYPE_CRASH = 1;
    public static final int TYPE_LIFE = 2;
    public static final int TYPE_NET = 3;
    public static final int TYPE_UI = 4;
    public static final int TYPE_METHOD= 5;
    private int type;
    private Parcelable realData;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Parcelable getRealData() {
        return realData;
    }

    public void setRealData(Parcelable realData) {
        this.realData = realData;
    }

    public TransformData(Parcelable realData) {
        this.realData = realData;
    }

    protected TransformData(Parcel in) {
        type = in.readInt();
        realData = in.readParcelable(TransformData.class.getClassLoader());
    }

    public static final Creator<TransformData> CREATOR = new Creator<TransformData>() {
        @Override
        public TransformData createFromParcel(Parcel in) {
            return new TransformData(in);
        }

        @Override
        public TransformData[] newArray(int size) {
            return new TransformData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeParcelable(realData,flags);
    }
}
