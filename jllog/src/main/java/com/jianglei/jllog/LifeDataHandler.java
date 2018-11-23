package com.jianglei.jllog;

import android.content.Context;
import android.os.Parcelable;

import com.jianglei.jllog.aidl.LifeVo;

/**
 * @author jianglei on 11/23/18.
 */

public class LifeDataHandler implements IDataHandler {
    private static LifeDataHandler instance;
    @Override
    public void handle(Parcelable parcelable, Context context) {
        if(!(parcelable instanceof LifeVo)){
            return;
        }
        LifeVo lifeVo = (LifeVo) parcelable;
        DataCenter.getInstance().addLifeVo(lifeVo);
        //生命周期的更新无需更新任何ui信息，因为这个不可能给会在后台更新
    }

    public static LifeDataHandler getInstance() {
        if(instance == null){
            instance = new LifeDataHandler();
        }
        return instance;
    }
}
