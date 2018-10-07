package com.jianglei.jlandroidlog;

import android.os.Bundle;
import android.view.View;

import com.jianglei.jllog.JlBaseActivity;
import com.jianglei.jllog.JlLog;
import com.jianglei.jllog.aidl.NetInfoVo;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends JlBaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_net).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetInfoVo netInfoVo = getNetInfo(true);

                JlLog.notifyNetInfo(netInfoVo);
            }
        });
        findViewById(R.id.btn_crash).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                NetInfoVo netInfoVo = null;
                netInfoVo.isSuccessful();
            }
        });

        findViewById(R.id.btn_net_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetInfoVo netInfoVo = getNetInfo(false);
                JlLog.notifyNetInfo(netInfoVo);
            }
        });
    }

    private NetInfoVo getNetInfo(boolean isSuccessful) {
        NetInfoVo netInfoVo = new NetInfoVo(isSuccessful);
        netInfoVo.setUrl("http://www.baidu.com");
        Map<String, String> header = new HashMap<>();
        header.put("name", "lei");
        netInfoVo.setRequestHeader(header);
        netInfoVo.setRequestForm(header);
        netInfoVo.setRequsetUrlParams(header);
        if(!isSuccessful){
            String error = "java.lang.RuntimeException: level 2 exception\n" +
                    "    at com.msh.demo.exceptionStack.Test.fun2(Test.java:17)\n" +
                    "    at com.msh.demo.exceptionStack.Test.main(Test.java:24)\n" +
                    "    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
                    "    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n" +
                    "    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n" +
                    "    at java.lang.reflect.Method.invoke(Method.java:498)\n" +
                    "    at com.intellij.rt.execution.application.AppMain.main(AppMain.java:147)\n" +
                    "Caused by: java.io.IOException: level 1 exception\n" +
                    "    at com.msh.demo.exceptionStack.Test.fun1(Test.java:10)\n" +
                    "    at com.msh.demo.exceptionStack.Test.fun2(Test.java:15)\n" +
                    "    ... 6 more";
            netInfoVo.setErrorMsg(error);
            return netInfoVo;

        }
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
    }
}
