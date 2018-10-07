package com.jianglei.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

public class StubTransform extends Transform {

    private Project mProject

    StubTransform(Project mProject) {
        this.mProject = mProject
    }

    @Override
    String getName() {
        return "StubTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        println("--------------------- start to insert log ------------------------------------")
        //遍历input

        def inputs = transformInvocation.inputs

        inputs.each { TransformInput input ->

            ////遍历jar文件 对jar不操作,只是将jar的路径加入类池，但是要输出到out路径
            input.jarInputs.each { JarInput jarInput ->
                // 重命名输出文件（同目录copyFile会冲突）
                def jarName = jarInput.name
                File jarFile = StubInjects.injectJar(jarInput, mProject)
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                def dest = transformInvocation.getOutputProvider().getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarFile, dest)
            }
            //遍历文件夹
            input.directoryInputs.each { DirectoryInput directoryInput ->
                //注入代码
                StubInjects.inject(directoryInput.file.absolutePath, mProject)
                // 获取output目录
                def dest = transformInvocation.getOutputProvider().getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                // 将input的目录复制到output指定目录
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }
        StubInjects.release()
        println("--------------------- finish inserting log ------------------------------------")
    }
}