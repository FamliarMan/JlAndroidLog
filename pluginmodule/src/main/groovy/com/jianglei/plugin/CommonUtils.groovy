package com.jianglei.plugin

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod

import java.lang.reflect.Modifier

public class CommonUtils {

    public static String getMethodName(CtMethod method) {
        def methodName = method.getName()
        return methodName.substring(
                methodName.lastIndexOf('.') + 1, methodName.length())
    }

    public static String getClassName(String filePath, String applicationId) {
        int index = filePath.indexOf(applicationId)
        if (index == -1) {
            return null
        }
        def name = filePath.substring(index, filePath.length() - ".class".length())
        return name.replace("/", ".")
    }

    public static boolean isActivity(CtClass ctClass) {
        if (ctClass.getSimpleName().contains("Activity") && (!ctClass.getSimpleName().contains('$'))) {
            return true
        }
        CtClass superClass = ctClass.getSuperclass()
        if (superClass == null || superClass.getClass().equals(Object.class)) {
            return false
        }
        return isActivity(superClass)
    }

    public
    static CtMethod createLifeCycleMethod(String methodName, ClassPool pool, CtClass activity, String insertStr) {
        if (methodName == "onCreate") {
            def params = new CtClass[1]
            params[0] = pool.getCtClass("android.os.Bundle")
            CtMethod method = new CtMethod(CtClass.voidType, methodName, params, activity)
            method.setModifiers(Modifier.PUBLIC)
            StringBuilder body = new StringBuilder()
            insertStr = insertStr.substring(1,insertStr.length()-1)
            body.append("{super.onCreate(\$1);\n").append(insertStr).append("}")
            method.setBody(body.toString())
            return method
        } else {
            CtMethod method = new CtMethod(CtClass.voidType, methodName, null, activity)
            method.setModifiers(Modifier.PUBLIC)
            StringBuilder body = new StringBuilder()
            insertStr = insertStr.substring(1,insertStr.length()-1)
            body.append("{super.").append(methodName).append("();").append("\n").append(insertStr).append("}")
            method.setBody(body.toString())
            return method
        }
    }

    public static boolean containMethod(List<CtMethod> methods, CtMethod method) {
        if(methods == null){
            return false
        }
        for (CtMethod m : methods) {
            if (m.equals(method)) {
                return true
            }
        }
        return false
    }
}