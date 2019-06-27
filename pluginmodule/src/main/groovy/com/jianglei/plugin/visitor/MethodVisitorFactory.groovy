package com.jianglei.plugin.visitor

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

public class MethodVisitorFactory {

    /**
     * 为不同类的不同方法提供不同的修改Visitor
     * @param className 类名
     * @param methodName 方法名
     * @param desc 方法描述符
     * @return
     */
    public static AdviceAdapter create(int api, MethodVisitor mv, int access, String name,
                                       String desc, String className) {
        //暂时全部返回TraceMethodVisitor
        return new TraceMethodVisitor(api, mv, access, name, desc, className)
    }

}