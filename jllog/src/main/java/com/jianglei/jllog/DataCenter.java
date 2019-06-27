package com.jianglei.jllog;

import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.LifeVo;
import com.jianglei.jllog.aidl.NetInfoVo;
import com.jianglei.jllog.uiblock.UiBlockVo;

import java.util.LinkedList;

/**
 * @author jianglei on 11/23/18.
 */

public class DataCenter {

    /**
     * crash记录
     */
    private LinkedList<CrashVo> crashVos = new LinkedList<>();
    /**
     * 网络信息记录
     */
    private LinkedList<NetInfoVo> netInfoVos = new LinkedList<>();
    /**
     * 生命周期信息记录
     */
    private LinkedList<LifeVo> lifeVos = new LinkedList<>();

    /**
     * ui阻塞信息
     */
    private LinkedList<UiBlockVo> uiBlockVos = new LinkedList<>();

    private static DataCenter instance = new DataCenter();

    public static DataCenter getInstance() {
        return instance;
    }

    public LinkedList<CrashVo> getCrashVos() {
        return crashVos;
    }

    public LinkedList<NetInfoVo> getNetInfoVos() {
        return netInfoVos;
    }

    public LinkedList<LifeVo> getLifeVos() {
        return lifeVos;
    }

    public LinkedList<UiBlockVo> getUiBlockVos() {
        return uiBlockVos;
    }

    /**
     * 增加一条ui阻塞信息
     *
     * @param uiBlockVo ui阻塞信息类
     */
    public void addUiTracer(UiBlockVo uiBlockVo) {
        if (uiBlockVos.size() >= JlLog.MAX_UI_RECORT) {
            uiBlockVos.remove(0);
        }
        uiBlockVos.add(uiBlockVo);
    }

    /**
     * 清除ui阻塞信息
     */
    public void clearUi() {
        uiBlockVos.clear();
    }

    /**
     * 增加一条crash记录
     *
     * @param crashVo crash信息类
     */
    public void addCrashVo(CrashVo crashVo) {
        if (crashVos.size() == JlLog.MAX_CRASH_RECORD) {
            crashVos.remove(0);
        }
        crashVos.add(crashVo);
    }

    /**
     * 清除crash数据
     */
    public void clearCrash() {
        crashVos.clear();
    }

    /**
     * 增加网络信息记录类
     *
     * @param netInfoVo 网络类
     */
    public void addNetInfoVo(NetInfoVo netInfoVo) {
        if (netInfoVos.size() == JlLog.MAX_NET_RECORD) {
            netInfoVos.remove(0);
        }
        netInfoVos.add(netInfoVo);
    }

    /**
     * 清除网络数据
     */
    public void clearNetInfo() {
        netInfoVos.clear();
    }

    /**
     * 增加一条生命周期信息
     *
     * @param lifeVo 生命周期详细类
     */
    public void addLifeVo(LifeVo lifeVo) {
        if (lifeVos.size() == JlLog.MAX_LIFE_RECORD) {
            lifeVos.remove(0);
        }
        lifeVos.add(lifeVo);
    }

    /**
     * 清除生命周期数据
     */
    public void clearLife() {
        lifeVos.clear();
    }
}
