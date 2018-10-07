package com.jianglei.plugin

import com.android.build.api.transform.JarInput
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.bytecode.ClassFile
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

class StubInjects {
    private static boolean isInit = false
    //初始化类池

    private static ClassPool pool = ClassPool.getDefault()


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

    static File injectJar(JarInput jarInput, Project project) {
        //将所有jar包的类路径加入
        pool.appendClassPath(jarInput.file.path)
        def jarName = jarInput.name
        def path = jarInput.file.path
        if (!jarName.startsWith(":")) {
            //说明该jar包是依赖的其他第三方包,无需处理，直接返回
            return jarInput.file
        }

        def unzipFilePath = path.substring(0, path.length() - 4) + "-unzip"
        ZipUtil.unzip(path, unzipFilePath)
        def hasInject = injectPath(unzipFilePath, project)
        if (!hasInject) {
            //该jar包没有内容需要插桩，直接返回原来的jar即可
            FileUtils.deleteDirectory(new File(unzipFilePath))
            return jarInput.file
        }
        FileUtils.forceDelete(jarInput.file)
        //将该目录重新压缩成jar包,并放回原位置
        ZipUtil.zip(unzipFilePath, path)
        FileUtils.deleteDirectory(new File(unzipFilePath))
        return new File(path)

    }

    /**
     * 对一个地址进行注入
     * @param path 路径
     * @param project project对象
     * @return 是否有注入
     */
    static boolean injectPath(String path, Project project) {
        initPool(project)
        //将当前路径加入类池,不然找不到这个类
        pool.appendClassPath(path)
        def dir = new File(path)
        if (!dir.isDirectory()) {
            return false
        }
        boolean hasInject = false
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
            //该类必须是activity且之前未被注入过才能注入
            if (CommonUtils.isActivity(ctClass, pool) && CommonUtils.isNeedInsert(ctClass)) {
                println("jllog:activity = " + ctClass.getName())
                realInsert(ctClass, path)
                hasInject = true
            }
            fin.close()

        }
        return hasInject
    }

    /**
     * 对该activity真正插入代码
     * @param activity 要插入代码的activity
     * @param path activity的具体路径
     * @return 无
     */
    static realInsert(CtClass activity, String path) {
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
