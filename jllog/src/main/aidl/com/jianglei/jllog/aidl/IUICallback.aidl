// IUICallback.aidl
package com.jianglei.jllog.aidl;
import com.jianglei.jllog.aidl.NetInfoVo;
import com.jianglei.jllog.aidl.CrashVo;
interface IUICallback {
    void updateNetInfo(in NetInfoVo netInfoVos);
    void updateCrashVo(in CrashVo crashVos);
}
