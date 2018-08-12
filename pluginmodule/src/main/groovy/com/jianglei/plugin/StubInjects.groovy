package com.jianglei.plugin

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.JarClassPath
import javassist.bytecode.Descriptor
import org.gradle.api.Project
import sun.management.MethodInfo

import java.lang.reflect.Modifier

public class StubInjects {
    //初始化类池

    private static ClassPool pool = new ClassPool(true)

    public static void injectJar(String path) {
        //将所有jar包的类路径加入
        def classPath = new JarClassPath(path)
        pool.appendClassPath(classPath)
    }

    public static void inject(String path, Project project, String applicationId) {
        //将当前路径加入类池,不然找不到这个类
        pool.appendClassPath(path);
        //project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类
        pool.appendClassPath(project.android.bootClasspath[0].toString());
        //引入android.os.Bundle包，因为onCreate方法参数有Bundle
        pool.importPackage("android.os.Bundle");
        pool.importPackage("com.jianglei.jllog.aidl.LifeVo")
        File dir = new File(path);
        if (dir.isDirectory()) {
            //遍历文件夹
            dir.eachFileRecurse { File file ->
                def filePath = file.absolutePath
                def name = file.getName()
                if (name.endsWith(".class") && !name.startsWith("R\$") &&
                        !"R.class".equals(name) && !"BuildConfig.class".equals(name)) {

                    String className = CommonUtils.getClassName(filePath, applicationId)
                    if (className != null) {
                        CtClass ctClass = pool.getCtClass(className);
                        if (CommonUtils.isActivity(ctClass)) {
                            println("ctClass = " + ctClass.getName())
                            insert(ctClass, path)
                        }
                    }
                }
            }
        }

    }

    public static insert(CtClass activity, String path) {
        if (activity.isFrozen()) {
            activity.defrost()
        }
        def methods = ["onCreate", "onStart", "onResume", "onRestart", "onStop", "onPause", "onDestroy"]
        def types = ["ON_CREATE", "ON_START", "ON_RESUME", "ON_RESTART", "ON_STOP", "ON_PAUSE", "ON_DESTROY"]
        List<CtMethod> declaredMethods = activity.getDeclaredMethods()
        for (int i = 0; i < methods.size(); ++i) {
            def insertStr = "{com.jianglei.jllog.aidl.LifeVo lifeVo = new com.jianglei.jllog.aidl.LifeVo(System.currentTimeMillis(),com.jianglei.jllog.aidl.LifeVo.TYPE_[[s]],getClass().getName());" +
                    "com.jianglei.jllog.JlLog.notifyLife(lifeVo);}"
            def name = methods.get(i)
            def type = types.get(i)
            insertStr = insertStr.replace("[[s]]", type)
            def needMethod = CommonUtils.createLifeCycleMethod(name, pool, activity, insertStr)
            if (!CommonUtils.containMethod(declaredMethods, needMethod)) {
                //该activity没有覆盖该生命周期方法
                activity.addMethod(needMethod)
            } else {
                CtMethod method;
                if (name == "onCreate") {
                    method = activity.getDeclaredMethod(name, pool.getCtClass("android.os.Bundle"))
                } else {
                    method = activity.getDeclaredMethod(name, null)
                }
                method.insertAfter(insertStr)
            }
        }
        activity.writeFile(path)
        activity.detach()
    }
}