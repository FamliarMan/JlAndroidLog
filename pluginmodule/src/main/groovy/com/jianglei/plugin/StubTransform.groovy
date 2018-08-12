package com.jianglei.plugin

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.io.FileUtils
import org.apache.commons.codec.digest.DigestUtils
import com.android.build.api.transform.Format
import org.gradle.api.Project


public  class StubTransform extends Transform{

    private Project mProject
    /**
     * 应用id
     */
    private String applicationId;

    StubTransform(Project mProject,String applicationId) {
        this.mProject = mProject
        this.applicationId = applicationId;
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
        //遍历input

        def inputs = transformInvocation.inputs

        inputs.each { TransformInput input ->

            ////遍历jar文件 对jar不操作，但是要输出到out路径
            input.jarInputs.each { JarInput jarInput ->
                StubInjects.injectJar(jarInput.file.getAbsolutePath())
                // 重命名输出文件（同目录copyFile会冲突）
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                def dest = transformInvocation.getOutputProvider().getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }
            //遍历文件夹
            input.directoryInputs.each { DirectoryInput directoryInput ->
                //注入代码
                StubInjects.inject(directoryInput.file.absolutePath, mProject,applicationId)

                // 获取output目录
                def dest = transformInvocation.getOutputProvider().getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)

                // 将input的目录复制到output指定目录
                FileUtils.copyDirectory(directoryInput.file, dest)
            }

        }
        System.out.println("--------------结束transform了----------------")
    }
}