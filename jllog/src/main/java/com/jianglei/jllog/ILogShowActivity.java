package com.jianglei.jllog;

import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.NetInfoVo;

import java.util.List;

/**
 * Created by jianglei on 5/12/18.
 */

public interface  ILogShowActivity {
    /**
     * 清除网络日志
     */
    void clearNet();

    /**
     * 清除崩溃日志
     */
    void clearCrash();

    List<NetInfoVo> getNetInfo();
    List<CrashVo> getCrashVo();
}
