package com.jianglei.jllog;

import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.LifeVo;
import com.jianglei.jllog.aidl.NetInfoVo;
import com.jianglei.jllog.uiblock.UiBlockVo;

import java.util.List;

/**
 * fragment和activity通信
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

    /**
     * 清除生命周期信息
     */
    void clearLife();

    /**
     * 获取所有生命周期信息
     */
    List<LifeVo> getLifeVos();

    /**
     * 清除ui阻塞信息
     */
    void clearUi();

    /**
     * 获取所有的ui阻塞信息
     */
    List<UiBlockVo> getUiTraces();
}
