package com.jianglei.jlandroidlog;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jianglei.jllog.JlLog;
import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.NetInfoVo;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;
    int k = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JlLog.start(getApplication());
        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetInfoVo netInfoVo = getNetInfo();
                JlLog.notifyNetInfo(netInfoVo);
            }
        });
        handler = new Handler(Looper.myLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
//                CrashVo crashVo = new CrashVo(System.currentTimeMillis());
//                crashVo.setCrashInfo("haha,崩溃了");
                NetInfoVo netInfoVo = getNetInfo();
                JlLog.notifyNetInfo(netInfoVo);
//                JlLog.notifyCrash(crashVo);
                k++;
                handler.postDelayed(runnable,2000);
            }
        };
//        handler.postDelayed(runnable,2000);
    }

    private NetInfoVo getNetInfo(){
        NetInfoVo netInfoVo = new NetInfoVo();
        netInfoVo.setUrl("www.baidu.com  "+k);
        Map<String,String>header = new HashMap<>();
        header.put("count",String.valueOf(k));
        header.put("name","lei");
        netInfoVo.setRequestHeader(header);
        netInfoVo.setRequestForm(header);
        netInfoVo.setRequsetUrlParams(header);
        String json = "[\n" +
                "                {\n" +
                "                    date: \"今天（周三）\",\n" +
                "                    dayPictureUrl: \"http://api.map.baidu.com/images/weather/day/duoyun.png\",\n" +
                "                    nightPictureUrl: \"http://api.map.baidu.com/images/weather/night/duoyun.png\",\n" +
                "                    weather: \"多云\",\n" +
                "                    wind: \"微风\",\n" +
                "                    temperature: \"23℃\"\n" +
                "                },\n" +
                "                {\n" +
                "                    date: \"明天（周四）\",\n" +
                "                    dayPictureUrl: \"http://api.map.baidu.com/images/weather/day/leizhenyu.png\",\n" +
                "                    nightPictureUrl: \"http://api.map.baidu.com/images/weather/night/zhongyu.png\",\n" +
                "                    weather: \"雷阵雨转中雨\",\n" +
                "                    wind: \"微风\",\n" +
                "                    temperature: \"29～22℃\"\n" +
                "                },\n" +
                "                {\n" +
                "                    date: \"后天（周五）\",\n" +
                "                    dayPictureUrl: \"http://api.map.baidu.com/images/weather/day/yin.png\",\n" +
                "                    nightPictureUrl: \"http://api.map.baidu.com/images/weather/night/duoyun.png\",\n" +
                "                    weather: \"阴转多云\",\n" +
                "                    wind: \"微风\",\n" +
                "                    temperature: \"31～23℃\"\n" +
                "                },\n" +
                "                {\n" +
                "                    date: \"大后天（周六）\",\n" +
                "                    dayPictureUrl: \"http://api.map.baidu.com/images/weather/day/duoyun.png\",\n" +
                "                    nightPictureUrl: \"http://api.map.baidu.com/images/weather/night/duoyun.png\",\n" +
                "                    weather: \"多云\",\n" +
                "                    wind: \"微风\",\n" +
                "                    temperature: \"31～24℃\"\n" +
                "                }\n" +
                "            ]";
        netInfoVo.setResponseJson(json);
        return netInfoVo;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
