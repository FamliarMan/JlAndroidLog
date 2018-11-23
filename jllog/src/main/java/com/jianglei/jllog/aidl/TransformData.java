package com.jianglei.jllog.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author jianglei on 11/22/18.
 */

public class TransformData implements Parcelable{
    private int type;
    private Parcelable realData;

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
        realData.writeToParcel(dest,flags);
    }
}
