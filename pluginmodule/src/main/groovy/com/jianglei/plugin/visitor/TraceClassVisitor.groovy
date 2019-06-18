package com.jianglei.plugin.visitor

import com.jianglei.plugin.visitor.TraceMethodVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

public class TraceClassVisitor extends ClassVisitor {

    private String className

    TraceClassVisitor(int i, ClassVisitor classVisitor) {
        super(i, classVisitor)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] exceptions) {
        super.visit(version, access, name, signature, superName, exceptions)
        this.className = name
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        def methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions)
        return MethodVisitorFactory.create(api, methodVisitor, access, name, desc, className)
    }
}

