package com.jianglei.jllog.uiblock;

import android.content.Context;
import android.os.Parcelable;

import com.jianglei.jllog.DataCenter;
import com.jianglei.jllog.IDataHandler;

/**
 * 处理ui阻塞的类
 *
 * @author jianglei on 12/23/18.
 */

public class UITracerHandler implements IDataHandler {
    private static UITracerHandler uiTracerHandler;

    @Override
    public void handle(Parcelable parcelable, Context context) {
        if (!(parcelable instanceof UiBlockVo)) {
            return;
        }
        UiBlockVo uiBlockVo = (UiBlockVo) parcelable;
        DataCenter.getInstance().addUiTracer(uiBlockVo);
    }

    @Override
    public void clear(Context context) {
        DataCenter.getInstance().clearNetInfo();

    }

    public static UITracerHandler getInstance() {
        if (uiTracerHandler == null) {
            uiTracerHandler = new UITracerHandler();
        }
        return uiTracerHandler;

    }
}
