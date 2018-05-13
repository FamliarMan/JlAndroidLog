# JlLog使用
## 依赖引入
（待续，等待jcenter审核）

## 初始化
```
JlLog.start(getApplication());
```
## 监控crash
这一步已经封装好，无需手动调用，当然，如果想主动发出crash信息也是可以的：
```
 CrashVo crashVo = new CrashVo(System.currentTimeMillis());
 crashVo.setCrashInfo("crash info");
 JlLog.notifyCrash(crashVo);
```
## 监控网络请求
如果你的app使用了okhttp，直接加入拦截器即可：
```
 okHttpClient.addInterceptor(new JlLogInterceptor());
```
如果你的app没有使用okhttp，那么你需要在自己的网路层手动调用监控方法：
```
NetInfoVo netInfoVo = new NetInfoVo();
//设置url
netInfoVo.setUrl("wwww.baidu.com");
//设置Query参数,即跟在url后面的参数
netInfoVo.setRequsetUrlParams(queryParams);
 //设置Header
netInfoVo.setRequestHeader(headers);
//设置post参数
netInfoVo.setRequestForm(postParams);
//设置返回信息json
netInfoVo.setResponseJson(rsponJson);
//将信息通知给监控服务
JlLog.notifyNetInfo(netInfoVo);
```

## 效果展示
![演示](http://7xpxx3.com1.z0.glb.clouddn.com/gif/blog/jllog.gif)


