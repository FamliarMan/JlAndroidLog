package com.jianglei.jllog;

import android.content.Context;
import android.os.Parcelable;

/**
 * @author jianglei on 11/23/18.
 */

public interface IDataHandler {
    /**
     * 处理发送过来的数据
     * @param parcelable 发送过来的数据
     * @param context  Android上下文
     */
    void handle(Parcelable parcelable,Context context);

    /**
     * 清空所有信息
     */
    void clear(Context context);
}
