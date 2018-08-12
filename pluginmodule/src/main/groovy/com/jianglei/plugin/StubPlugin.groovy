package com.jianglei.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class StubPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        System.out.println("========================");
        System.out.println("hello gradle plugin!");
        System.out.println("========================");
        def android = project.extensions.findByType(AppExtension.class)
        //注册一个Transform
        def applicationId
        android.applicationVariants.all {variant ->
            applicationId = [variant.mergedFlavor.applicationId, variant.buildType.applicationIdSuffix].findAll().join()
            println("buildtype: "+ variant.buildType)
            println("applicationId: "+ applicationId)
        }
        println("applicationId last: "+ applicationId)
//        if(applicationId == null || applicationId.size() == 0){
//            throw new IllegalArgumentException("Sorry,you should specify an application id in your build file")
//        }
        def classTransform = new StubTransform(project,"com/jianglei/jlandroidlog");
        android.registerTransform(classTransform);

    }
}