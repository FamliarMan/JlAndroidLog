package com.jianglei.jllog;

import android.content.Context;

import com.jianglei.jllog.aidl.CrashVo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author jianglei
 */

public class JlCrashHandler implements Thread.UncaughtExceptionHandler {

    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 单例引用，这里我们做成单例的，因为我们一个应用程序里面只需要一个UncaughtExceptionHandler实例
     */
    private static JlCrashHandler instance;

    private JlCrashHandler() {
    }

    public synchronized static JlCrashHandler getInstance() {  //同步方法，以免单例多线程环境下出现异常
        if (instance == null) {
            instance = new JlCrashHandler();
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public void init(Context context) {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        handleException(e);
        mDefaultHandler.uncaughtException(t, e);

    }


    private void handleException(Throwable ex) {
        if (ex == null) {
            return;
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        CrashVo crashVo = new CrashVo(System.currentTimeMillis());
        crashVo.setCrashInfo(result);
        JlLog.notifyCrash(crashVo);
    }
}
