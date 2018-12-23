package com.jianglei.jllog.uiblock;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;

import com.jianglei.jllog.JlLog;


/**
 * @author jianglei on 11/27/18.
 */

public class LoopPrinter implements Printer {
    /**
     * 是否是第一次打印
     */
    private boolean isFirstPrint = true;
    private long startTime;
    /**
     * ui阻塞时间,单位为s，默认6秒
     */
    private int thresholdTime = 6;
    private String stackTrace;

    private Runnable dumpRunnable;

    /**
     * dump 堆栈信息的handler
     */
    private Handler dumpHandler;
    /**
     * 用来将堆栈信息发送到log进程的handler
     */
    private Handler sendStackTraceHandler;

    public LoopPrinter(int thresholdTime, Handler dumpHandler, Handler sendStackTraceHandler) {
        this.thresholdTime = thresholdTime;
        this.dumpHandler = dumpHandler;
        this.sendStackTraceHandler = sendStackTraceHandler;
    }

    @Override
    public void println(String x) {
        if (isFirstPrint) {
            //任务执行前
            startTime = System.currentTimeMillis();
            isFirstPrint = false;
            startDump();
        } else {
            //任务执行后
            long endTime = System.currentTimeMillis();
            long blockTime = endTime - startTime;
            if (blockTime >= thresholdTime * 1000) {
                //触发了ui阻塞
                handStackTrace(blockTime);
            } else {
                stopDump();
            }
            isFirstPrint = true;


        }

    }

    /**
     * dump当前堆栈信息
     */
    public void startDump() {
        dumpRunnable = new Runnable() {
            @Override
            public void run() {
                StackTraceElement[] elements = Looper.getMainLooper().getThread().getStackTrace();
                StringBuilder sb = new StringBuilder();
                for (StackTraceElement e : elements) {
                    sb.append(e.toString()).append("\n");
                }
                stackTrace = sb.toString();

            }
        };
        dumpHandler.postDelayed(dumpRunnable, (long) (thresholdTime * 0.8 * 1000));
    }

    /**
     * 停止dump堆栈信息
     */
    public void stopDump() {

        if (dumpHandler == null) {
            return;
        }
        dumpHandler.removeCallbacks(dumpRunnable);
    }


    /**
     * 处理导致ui 阻塞的堆栈信息
     */
    public void handStackTrace(final long blockTime) {
        sendStackTraceHandler.post(new Runnable() {
            @Override
            public void run() {
                UiBlockVo uiBlockVo = new UiBlockVo(stackTrace, blockTime, System.currentTimeMillis());
                JlLog.notifyUiBlock(uiBlockVo);

            }
        });
    }

}
