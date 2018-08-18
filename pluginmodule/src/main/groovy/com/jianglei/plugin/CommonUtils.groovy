package com.jianglei.plugin

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod

import java.lang.reflect.Modifier

class CommonUtils {

    static boolean isActivity(CtClass ctClass,ClassPool pool) {
        def activity = pool.getCtClass("android.app.Activity")
        return ctClass.subclassOf(activity)
    }

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

    static boolean containMethod(List<CtMethod> methods, CtMethod method) {
        if(methods == null){
            return false
        }
        for (CtMethod m : methods) {
            if (m == method) {
                return true
            }
        }
        return false
    }
}