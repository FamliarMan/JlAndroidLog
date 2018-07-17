# JlLog使用
## 依赖引入
```
compile('com.jianglei:jllog:1.2')
```
如果出现support包冲突无法解决，首选升级buildtool 到26，如果实在不想升级可用下列版本，该版本不再继续维护
```
compile('com.jianglei:jllog:0.2')
```

## 初始化
```
JlLog.start(getApplication(),true);
```
第二个参数注意，如果当前是开发版本就传true,日志工具此时会生效，如果是发布版本就传false，日志工具不会启动。
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
如果你的app没有使用okhttp，那么你需要在自己的网络层手动调用监控方法：
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

## 更新记录

#### 1.2
增加了网络异常的展示，优化了activity任务栈

