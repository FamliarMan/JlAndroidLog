package com.jianglei.jllog;

import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.NetInfoVo;

import java.util.List;

/**
 * @author jianglei
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

    /**
     * 获取所有网络日志信息
     * @return 返回list对象
     */
    List<NetInfoVo> getNetInfo();

    /**
     * 获取所有crash信息
     * @return 返回list对象
     */
    List<CrashVo> getCrashVo();
}
