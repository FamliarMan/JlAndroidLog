# JlLog使用
## 依赖引入
```
compile('com.jianglei:jllog:2.0')
```


## 初始化
```
//此处传入的5代表ui阻塞超过5s就会被记录
JlLog.start(getApplication(),5,true);
```
第三个参数注意，如果当前是开发版本就传true,日志工具此时会生效，如果是发布版本就传false，日志工具不会启动。

## 监控所有方法耗时
这里采用了字节码插桩的方式，所以需要引入gradle插件

首先，在项目根目录下的build.gradle插件下如下配置：
```

buildscript {
    dependencies {
        classpath 'com.android.tools.build:gradle:3.4.0'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath 'com.jianglei:jllog-tracer:1.0'
    }
}

```
这里主要是为了引入这个插件依赖，你如果不想在这里配置，也可以在每个module的
build.gradle文件中如此配置依赖

然后，在每个module的build.gradle中引入插件
```
apply plugin: 'com.jianglei.jllog'
```

默认是不会监控第三方库的方法的，如果想要监控第三方库，可以在app模块下的build.gradle
中添加配置：
```

jlLog{
    
    traceThirdLibrary = true
}
```

这样就可以了，注意，想要用这个功能，jllog版本最低是2.0

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
## 监控生命周期
这一步已经封装好，无需手动调用

## 效果展示

<img src="other/example.gif" width="320" height="480"/>


## 更新记录
#### 1.5
增加ui阻塞（anr）的监控
修改了UI样式
#### 1.4
增加了生命周期的监控
#### 1.2
增加了网络异常的展示，优化了activity任务栈

