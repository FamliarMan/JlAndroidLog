package com.jianglei.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class StubPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def android = project.extensions.findByType(AppExtension.class)
        //注册一个Transform
        def classTransform = new StubTransform(project)
        android.registerTransform(classTransform)

    }
}