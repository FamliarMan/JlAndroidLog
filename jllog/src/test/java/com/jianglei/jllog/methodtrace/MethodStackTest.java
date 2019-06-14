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
    public void setup(){
        PowerMockito.mockStatic(LogUtils.class);
    }

    @Test
    public void addMethodTrace_in() {
        MethodTraceInfo info = new MethodTraceInfo("testClass","testMethod",
                System.currentTimeMillis(),MethodTraceInfo.IN);
        MethodStack stack = MethodStack.getInstance();
        stack.addMethodTrace(info);
        //第一层节点里面应该有这个方法信息
        MethodStack.MethodNode node = stack.getFirstLevelNode().get(0);
        assertEquals(node.getClassNameAndHash(),"testClass");
        //索引里面有这个节点
        assertTrue(stack.getIndex().containsKey(node.getClassNameAndHash()));


        //增加一个方法in信息
        long time = System.currentTimeMillis();
        info = new MethodTraceInfo("childClass","testMethod",
                time,MethodTraceInfo.IN);
        //第一层节点应该没有这个信息，所以第一层节点数量为1
        stack.addMethodTrace(info);
        assertEquals(1,stack.getFirstLevelNode().size());
        assertTrue(stack.getIndex().containsKey("childClass"));
        assertEquals("childClass", stack.getFirstLevelNode().get(0).getChildNodes().get(0)
                .getClassNameAndHash());

        //增加一个方法out信息
        info = new MethodTraceInfo("childClass","testMethod",
                time+1,MethodTraceInfo.OUT);
        stack.addMethodTrace(info);
        //时间小于5ms，上一个进入节点应该被删除
        assertFalse(stack.getIndex().containsKey("childClass"));

        //继续增加一个方法out信息，但这个方法执行时间超过5ms
        info = new MethodTraceInfo("testClass","testMethod",
                time+6,MethodTraceInfo.OUT);
        stack.addMethodTrace(info);
        assertTrue(stack.getIndex().containsKey("testClass"));
        assertEquals(1, stack.getIndex().size());
        assertNull(stack.getLastUnFinishedNode());

        //继续增加一个in信息，应该有两个一层节点了

        info = new MethodTraceInfo("testClass2","testMethod",
                System.currentTimeMillis(),MethodTraceInfo.IN);
        stack.addMethodTrace(info);
        assertEquals(2,stack.getFirstLevelNode().size());



    }
}