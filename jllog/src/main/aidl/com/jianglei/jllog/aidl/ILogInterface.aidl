// ILogInterface.aidl
package com.jianglei.jllog.aidl;
import com.jianglei.jllog.aidl.NetInfoVo;
import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.IUICallback;

interface ILogInterface {

    //通知有新的网络请求
    void notifyNetInfo(in NetInfoVo netInfoVo);
    //通知有新的crash
    void notifyCrash(in CrashVo crashVo);
    List<NetInfoVo> getNetInfoVos();
    List<CrashVo> getCrashVos();
}
