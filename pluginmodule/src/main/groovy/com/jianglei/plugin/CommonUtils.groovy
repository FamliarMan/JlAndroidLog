package com.jianglei.plugin

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.bytecode.Bytecode
import javassist.bytecode.CodeAttribute

import java.lang.reflect.Modifier

class CommonUtils {

    static boolean isActivity(CtClass ctClass,ClassPool pool) {
        def activity = pool.getCtClass(MethodConstants.ACTIVITY)
        return ctClass.subclassOf(activity)
    }

    static CtMethod createLifeCycleMethod(String methodName, ClassPool pool, CtClass activity, String insertStr) {
        if (methodName == "onCreate") {
            def params = new CtClass[1]
            params[0] = pool.getCtClass("android.os.Bundle")
            CtMethod method = new CtMethod(CtClass.voidType, methodName, params, activity)
            method.setModifiers(activity.getModifiers() & ~Modifier.ABSTRACT)
            StringBuilder body = new StringBuilder()
            insertStr = insertStr.substring(1,insertStr.length()-1)
            body.append("{super.onCreate(\$1);\n").append(insertStr).append("}")
            method.setBody(body.toString())
            return method
        } else {
            CtMethod method = new CtMethod(CtClass.voidType, methodName, null, activity)
            method.setModifiers(activity.getModifiers() & ~Modifier.ABSTRACT)
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

    /**
     * 探测某个方法是否包含某句代码
     * @param method 要探测的方法
     * @param insert  是否被包含的语句
     * @return 如果包含，返回true，否则返回false
     */
    static boolean isNeedInsert(CtMethod  method,Bytecode insertStrCode){

        def methodInfo = method.getMethodInfo2()
        def attribute = methodInfo.getCodeAttribute()
        def iterator = attribute.iterator()
        while(iterator.hasNext()){
            println(++iterator.toString() +"  "+ insertStrCode.toString())
        }
        return false

    }
}