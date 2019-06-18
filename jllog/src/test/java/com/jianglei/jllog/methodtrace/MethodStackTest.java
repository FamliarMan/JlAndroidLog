package com.jianglei.jllog.methodtrace;

import com.jianglei.jllog.utils.LogUtils;

import org.hamcrest.core.AnyOf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

/**
 * @author longyi created on 19-6-14
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LogUtils.class})
public class MethodStackTest {
    @Before
    public void setup() {
        PowerMockito.mockStatic(LogUtils.class);
    }

    /**
     * 最简单的，只有一个第一级方法调用信息
     */
    @Test
    public void addMethodTrace_1() {
        MethodTraceInfo info = new MethodTraceInfo("testClass", "testMethod", "()V",
                System.currentTimeMillis(), MethodTraceInfo.IN);
        MethodStack stack = MethodStack.getInstance();
        stack.addMethodTrace(info);
        //第一层节点里面应该有这个方法信息
        MethodStack.MethodNode node = stack.getFirstLevelNode().get(0);
        assertEquals(node.getClassNameAndHash(), "testClass");
        //索引里面有这个节点
        assertTrue(stack.getIndex().containsKey(node.getClassNameAndHash()));
        //out信息
        info = new MethodTraceInfo("testClass", "testMethod", "()V",
                System.currentTimeMillis(), MethodTraceInfo.OUT);
        stack.addMethodTrace(info);

        assertEquals(1, stack.getFirstLevelNode().size());
        assertEquals(1, stack.getIndex().size());


    }

    /**
     * 同一个第一级方法被多次调用，应该保留时间长的
     */
    @Test
    public void addMethodTrace_2() {

        MethodStack stack = MethodStack.getInstance();
        stack.reset();

        //增加一个方法in信息
        long time = System.currentTimeMillis();

        //第一次调用这个方法
        MethodTraceInfo info = new MethodTraceInfo("testClass", "testMethod", "()V",
                time, MethodTraceInfo.IN);
        stack.addMethodTrace(info);
        info = new MethodTraceInfo("testClass", "testMethod", "()V",
                time + 100, MethodTraceInfo.OUT);
        stack.addMethodTrace(info);

        //第二次调用,方法执行时间比上次短,这个节点应该会被忽略
        info = new MethodTraceInfo("testClass", "testMethod", "()V",
                time+200, MethodTraceInfo.IN);
        stack.addMethodTrace(info);
        info = new MethodTraceInfo("testClass", "testMethod", "()V",
                time + 250, MethodTraceInfo.OUT);
        stack.addMethodTrace(info);
        assertEquals(1, stack.getFirstLevelNode().size());
        assertTrue(stack.getIndex().containsKey("testClass"));
        assertEquals(1,stack.getIndex().size());


        //第三次调用,方法执行时间比第一次长，这个节点的执行时间应该更新成本次时间
        info = new MethodTraceInfo("testClass", "testMethod", "()V",
                time+300, MethodTraceInfo.IN);
        stack.addMethodTrace(info);
        info = new MethodTraceInfo("testClass", "testMethod", "()V",
                time + 550, MethodTraceInfo.OUT);
        stack.addMethodTrace(info);
        assertEquals(1, stack.getFirstLevelNode().size());
        assertTrue(stack.getIndex().containsKey("testClass"));
        assertEquals(1,stack.getIndex().size());
        assertEquals(250,stack.getFirstLevelNode().get(0).getTime());


    }


    /**
     * 测试子级方法调用情况
     */
    @Test
    public void addMethodTrace_3() {

        MethodStack stack = MethodStack.getInstance();
        stack.reset();

        //增加一个方法in信息
        long time = System.currentTimeMillis();

        //第一层节点方法进入
        MethodTraceInfo info = new MethodTraceInfo("testClass", "testMethod", "()V",
                time, MethodTraceInfo.IN);
        stack.addMethodTrace(info);
        //第二层节点方法进入
        info = new MethodTraceInfo("childClass", "testMethod", "()V",
                time + 1, MethodTraceInfo.IN);
        stack.addMethodTrace(info);

        //第二层节点方法退出
        info = new MethodTraceInfo("childClass", "testMethod", "()V",
                time+200, MethodTraceInfo.OUT);
        stack.addMethodTrace(info);
        assertEquals(1,stack.getFirstLevelNode().size());
        assertEquals(1,stack.getFirstLevelNode().get(0).getChildNodes().size());

        //第一层节点方法退出
        info = new MethodTraceInfo("testClass", "testMethod", "()V",
                time + 250, MethodTraceInfo.OUT);
        stack.addMethodTrace(info);
        assertEquals(1, stack.getFirstLevelNode().size());
        assertTrue(stack.getIndex().containsKey("testClass"));
        assertEquals(2,stack.getIndex().size());


        //第三次调用,方法执行时间比第一次长，这个节点的执行时间应该更新成本次时间
        info = new MethodTraceInfo("testClass", "testMethod", "()V",
                time+300, MethodTraceInfo.IN);
        stack.addMethodTrace(info);
        info = new MethodTraceInfo("testClass", "testMethod", "()V",
                time + 550, MethodTraceInfo.OUT);
        stack.addMethodTrace(info);
        assertEquals(1, stack.getFirstLevelNode().size());
        assertTrue(stack.getIndex().containsKey("testClass"));
        assertEquals(2,stack.getIndex().size());
        assertEquals(250,stack.getFirstLevelNode().get(0).getTime());
    }


    /**
     * 测试子级方法多次调用情况
     */
    @Test
    public void addMethodTrace_4() {

        MethodStack stack = MethodStack.getInstance();
        stack.reset();

        //增加一个方法in信息
        long time = System.currentTimeMillis();

        //第一层节点方法进入
        MethodTraceInfo info = new MethodTraceInfo("testClass", "testMethod", "()V",
                time, MethodTraceInfo.IN);
        stack.addMethodTrace(info);
        //第二层节点方法第一次调用
        info = new MethodTraceInfo("childClass", "testMethod", "()V",
                time , MethodTraceInfo.IN);
        stack.addMethodTrace(info);
        info = new MethodTraceInfo("childClass", "testMethod", "()V",
                time+200, MethodTraceInfo.OUT);
        stack.addMethodTrace(info);
        assertEquals(200,stack.getFirstLevelNode().get(0).getChildNodes().get(0).getTime());

        //第二层节点方法第二次调用,调用时间比第一次短
        info = new MethodTraceInfo("childClass", "testMethod", "()V",
                time + 250, MethodTraceInfo.IN);
        stack.addMethodTrace(info);

        info = new MethodTraceInfo("childClass", "testMethod", "()V",
                time + 300, MethodTraceInfo.OUT);
        stack.addMethodTrace(info);
        assertEquals(1, stack.getFirstLevelNode().size());
        assertEquals(200,stack.getFirstLevelNode().get(0).getChildNodes().get(0).getTime());

        //第二层节点方法第三次调用，调用时间比第一次长
        info = new MethodTraceInfo("childClass", "testMethod", "()V",
                time + 400, MethodTraceInfo.IN);
        stack.addMethodTrace(info);

        info = new MethodTraceInfo("childClass", "testMethod", "()V",
                time + 700, MethodTraceInfo.OUT);
        stack.addMethodTrace(info);
        assertEquals(1, stack.getFirstLevelNode().size());
        assertEquals(300,stack.getFirstLevelNode().get(0).getChildNodes().get(0).getTime());

    }


    /**
     * 测试方法递归调用
     */
    @Test
    public void addMethodTrace_5() {
        MethodStack stack = MethodStack.getInstance();
        MethodTraceInfo info = new MethodTraceInfo("testClass", "testMethod", "()V",
                System.nanoTime(), MethodTraceInfo.IN);
        stack.addMethodTrace(info);
        info = new MethodTraceInfo("testClass", "testMethod", "()V",
                System.nanoTime(), MethodTraceInfo.IN);
        stack.addMethodTrace(info);
        info = new MethodTraceInfo("testClass", "testMethod", "()V",
                System.nanoTime(), MethodTraceInfo.IN);
        stack.addMethodTrace(info);
        //out信息
        info = new MethodTraceInfo("testClass", "testMethod", "()V",
                System.nanoTime(), MethodTraceInfo.OUT);
        stack.addMethodTrace(info);
        info = new MethodTraceInfo("testClass", "testMethod", "()V",
                System.nanoTime(), MethodTraceInfo.OUT);
        stack.addMethodTrace(info);
        info = new MethodTraceInfo("testClass", "testMethod", "()V",
                System.nanoTime(), MethodTraceInfo.OUT);
        stack.addMethodTrace(info);

        assertEquals(1, stack.getFirstLevelNode().size());
        assertEquals(1, stack.getFirstLevelNode().get(0).getChildNodes().size());
        assertEquals(1, stack.getIndex().size());


    }
}

