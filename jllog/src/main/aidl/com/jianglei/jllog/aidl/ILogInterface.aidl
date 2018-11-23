// ILogInterface.aidl
package com.jianglei.jllog.aidl;
import com.jianglei.jllog.aidl.NetInfoVo;
import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.LifeVo;
import com.jianglei.jllog.aidl.TransformData;
import com.jianglei.jllog.aidl.IUICallback;

interface ILogInterface {


    //退出服务
    void exit();

    //通知数据更新
    void notifyData(in TransformData data);

    //清除数据
    void clearData(in int type);

}
