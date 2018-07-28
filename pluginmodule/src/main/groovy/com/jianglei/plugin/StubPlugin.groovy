package com.jianglei.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

public  class StubPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        System.out.println("========================");
        System.out.println("hello gradle plugin!");
        System.out.println("========================");

    }
}