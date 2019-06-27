package com.jianglei.plugin.visitor


import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

public class TraceMethodVisitor extends AdviceAdapter {

    private String className
    private String methodName
    private String desc
    private int access
    //某个方法是否需要追踪
    private boolean isNeedTraceMethod

    protected TraceMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc, String className) {
        super(api, mv, access, name, desc)
        this.className = className
        this.methodName = name
        this.desc = desc
        this.access = access
        isNeedTraceMethod = !(name == "<init>" || name == "<clinit>" || name == "hashCode")
    }

    @Override
    void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, type)
    }

    @Override
    void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf)
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter()
        if (!isNeedTraceMethod) {
            super.onMethodEnter()
            return
        }
        mv.visitLdcInsn(className)

        if (isStatic(access)) {
            mv.visitInsn(ICONST_0)
        } else {
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "identityHashCode",
                    "(Ljava/lang/Object;)I", false)
        }
        mv.visitLdcInsn(methodName)
        mv.visitLdcInsn(desc)
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false)
        mv.visitMethodInsn(INVOKESTATIC, "com/jianglei/jllog/methodtrace/MethodTracer",
                "i", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;J)V", false)


    }

    @Override
    protected void onMethodExit(int opcode) {
        if (!isNeedTraceMethod) {
            return
        }
        mv.visitLdcInsn(className)

        if (isStatic(access)) {
            mv.visitInsn(ICONST_0)
        } else {
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "identityHashCode",
                    "(Ljava/lang/Object;)I", false)
        }

        mv.visitLdcInsn(methodName)
        mv.visitLdcInsn(desc)
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false)
        mv.visitMethodInsn(INVOKESTATIC, "com/jianglei/jllog/methodtrace/MethodTracer",
                "o", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;J)V", false)
    }


    /**
     * 判断某个访问标记是否是静态的，主要判断从右往左的第4位是否为1
     * @param access 访问标记
     * @return 是静态的返回true
     */
    private static boolean isStatic(int access) {
        return (access & 8) != 0
    }
}