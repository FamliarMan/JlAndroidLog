// ILogInterface.aidl
package com.jianglei.jllog.aidl;
import com.jianglei.jllog.aidl.NetInfoVo;
import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.LifeVo;
import com.jianglei.jllog.aidl.IUICallback;

interface ILogInterface {

    //通知有新的网络请求
    void notifyNetInfo(in NetInfoVo netInfoVo);
    //清除所有网络请求通知
    void clearNetInfo();
    //获取所有的网络请求信息
    List<NetInfoVo> getNetInfoVos();
    //通知有新的crash
    void notifyCrash(in CrashVo crashVo);
    //清除所有的crash
    void clearCrash();
    //获取所有的crash信息
    List<CrashVo> getCrashVos();

    //通知新的生命周期被触发
    void notifyLife(in LifeVo lifeVo);
    //清除所有的生命周期信息
    void clearLife();
    //获取所有的生命周期信息
    List<LifeVo> getLifeVos();

    //退出服务
    void exit();

}
