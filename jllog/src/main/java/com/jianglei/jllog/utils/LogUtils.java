package com.jianglei.jllog.utils;

import android.util.Log;

/**
 * @author longyi created on 19-6-14
 */
public class LogUtils {
    private static final String TAG = "JL-Log";
    private static boolean open = false;

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void d(String msg) {
        if(open) {
            Log.d(TAG, msg);
        }
    }
    public static void d(String tag,String msg) {
        if(open){
            Log.d(TAG+"-"+tag, msg);
        }
    }

}
