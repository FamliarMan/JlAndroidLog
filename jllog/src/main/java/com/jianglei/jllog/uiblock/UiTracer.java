package com.jianglei.jllog.uiblock;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * @author jianglei on 11/27/18.
 */

public class UiTracer {
    /**
     * ui阻塞时间阈值，单位为s
     */
    public static int threshold = 4;

    private static Handler dumpHandler;
    /**
     * 用来将堆栈信息发送到log进程的handler
     */
    private static Handler sendStackTraceHandler;

    /**
     * 开始监控ui阻塞
     * @param threshold 触发监控的时间，单位为s
     */
    public static void start( int threshold) {
        HandlerThread dumpHandlerThread = new HandlerThread("dump");
        dumpHandlerThread.start();
        dumpHandler = new Handler(dumpHandlerThread.getLooper());


        HandlerThread sendStackTraceThread = new HandlerThread("sendStackTrace");
        sendStackTraceThread.start();
        sendStackTraceHandler = new Handler(sendStackTraceThread.getLooper());
        Looper.getMainLooper().setMessageLogging(new LoopPrinter(threshold,dumpHandler,sendStackTraceHandler));
    }

    public static Handler getDumpHandler() {
        return dumpHandler;
    }

    public static Handler getSendStackTraceHandler() {
        return sendStackTraceHandler;
    }
}
