package com.jianglei.plugin

import com.android.build.api.transform.JarInput
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.bytecode.ClassFile
import org.gradle.api.Project

class StubInjects {
    private static boolean isInit = false
    //初始化类池

    private static ClassPool pool = new ClassPool(true)

    private static void initPool(Project project) {
        if (isInit) {
            return
        }
        //project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类
        pool.appendClassPath(project.android.bootClasspath[0].toString())
        //引入android.os.Bundle包，因为onCreate方法参数有Bundle
        pool.importPackage("android.os.Bundle")
        isInit = true
    }

    static File injectJar(JarInput jarInput,Project project) {
        //将所有jar包的类路径加入
        pool.appendClassPath(jarInput.file.path)
        def jarName = jarInput.name
        def path = jarInput.file.path
        if (!jarName.startsWith(":")) {
            //说明该jar包是依赖的其他第三方包,无需处理，直接返回
            return jarInput.file
        }

        def unzipFilePath = path.substring(0,path.length()-4) + "-unzip"
        ZipUtil.unzip(path, unzipFilePath)
        inject(unzipFilePath,project)
        //将该目录重新压缩成jar包
        String newJarPath = path+"-zip.jar"
        ZipUtil.zip(unzipFilePath,newJarPath)
        println("unzip jar: "+ newJarPath)
        return new File(newJarPath)

    }


    static void inject(String path, Project project) {
        initPool(project)
        //将当前路径加入类池,不然找不到这个类
        pool.appendClassPath(path)
        def dir = new File(path)
        if (!dir.isDirectory()) {
            return
        }
        //遍历文件夹
        dir.eachFileRecurse { File file ->
            def filePath = file.absolutePath
            def name = file.getName()
            if (!name.endsWith(".class") || name.startsWith("R\$") ||
                    "R.class" == name || "BuildConfig.class" == name) {
                return
            }
            def fin = new BufferedInputStream(new FileInputStream(filePath))
            def classFile = new ClassFile(new DataInputStream(fin))
            CtClass ctClass = pool.makeClass(classFile)
            if (CommonUtils.isActivity(ctClass, pool)) {
                println("jllog:activity = " + ctClass.getName())
                insert(ctClass, path)
            }
        }

    }


    static insert(CtClass activity, String path) {
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
                CtMethod method
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