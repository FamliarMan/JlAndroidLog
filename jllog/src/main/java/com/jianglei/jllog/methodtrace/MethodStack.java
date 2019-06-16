package com.jianglei.jllog.methodtrace;

import com.jianglei.jllog.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author longyi created on 19-6-14
 */
public class MethodStack {
    private static MethodStack instance = new MethodStack();

    public static MethodStack getInstance() {
        return instance;
    }

    /**
     * 这个索引主要是为了加速根据类名的查找速度,
     * 所以此处key是类名@hashcode, value是该类下所有的方法调用节点
     */
    private Map<String, List<MethodNode>> index = new HashMap<>();

    /**
     * 最后一个未完成的方法节点
     */
    private MethodNode lastUnFinishedNode;

    /**
     * 第一层节点
     */
    private List<MethodNode> firstLevelNode = new ArrayList<>();


    public void addMethodTrace(MethodTraceInfo info) {
        MethodNode node = new MethodNode(info.getClassNameAndHash(), info.getMethodName(),
                info.getTime());
        if (info.getType() == MethodTraceInfo.IN) {
            //这个方法第一次被执行,有两种情况，一种是第一级的节点，比如某个Activity的onCreate方法，
            //或者是在某个方法内部被调用
            if (lastUnFinishedNode == null) {
                //说明这是一个第一级节点
                firstLevelNode.add(node);
            } else {
                //说明这个方法在某个其他方法内部被第一次调用
                lastUnFinishedNode.addNode(node);
                node.parentNode = lastUnFinishedNode;
            }
            lastUnFinishedNode = node;

        } else {
            if (lastUnFinishedNode == null) {
                //异常情况，没有方法的进入信息，本条方法退出信息会被抛弃
                LogUtils.w("异常节点，没有该方法的进入信息，此节点会被抛弃");
                return;
            }
            if (!lastUnFinishedNode.getClassNameAndHash().equals(info.getClassNameAndHash())
                    || !lastUnFinishedNode.getMethodName().equals(info.getMethodName())) {
                LogUtils.w("异常节点，该方法的退出信息和上一个方法的进入信息不符合，此节点会被抛弃");
                return;
            }
            lastUnFinishedNode.outTime = info.getTime();
            lastUnFinishedNode.time = (int) (lastUnFinishedNode.outTime - lastUnFinishedNode.inTime);
            lastUnFinishedNode = lastUnFinishedNode.parentNode;
        }

        List<MethodNode> nodes = index.get(info.getClassNameAndHash());
        if (nodes == null) {
            nodes = new ArrayList<>();
        }
        nodes.add(node);
        index.put(info.getClassNameAndHash(), nodes);

    }

    public Map<String, List<MethodNode>> getIndex() {
        return index;
    }

    public MethodNode getLastUnFinishedNode() {
        return lastUnFinishedNode;
    }

    public List<MethodNode> getFirstLevelNode() {
        return firstLevelNode;
    }

    public static class MethodNode {

        /**
         * 类名以及hashcode的结合体,类似于MainActivity@13534645423
         */
        private String classNameAndHash;

        /**
         * 方法名称
         */
        private String methodName;

        /**
         * 方法执行时间,ns
         */
        private int time;

        /**
         * 方法进入时的时间ns，用System.nanoTime()获取
         */
        private long inTime;

        /**
         * 方法退出时的时间ns，用System.nanoTime()获取
         */
        private long outTime;

        /**
         * 方法是否执行完毕，如果只执行了进入没有退出为false，否则为true
         */
        private boolean isFinished;
        /**
         * 内部方法调用节点
         */
        private List<MethodNode> childNodes = new ArrayList<>();

        /**
         * 该方法的上级调用方法，可以为空
         */
        private MethodNode parentNode;

        public MethodNode(String classNameAndHash, String methodName, int time) {
            this.classNameAndHash = classNameAndHash;
            this.methodName = methodName;
            this.time = time;
            this.isFinished = true;
        }

        public MethodNode(String classNameAndHash, String methodName, long inTime) {
            this.classNameAndHash = classNameAndHash;
            this.methodName = methodName;
            this.isFinished = false;
            this.inTime = inTime;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public String getClassNameAndHash() {
            return classNameAndHash;
        }

        public String getMethodName() {
            return methodName;
        }

        public int getTime() {
            return time;
        }

        public void addNode(MethodNode node) {
            childNodes.add(node);
        }

        public List<MethodNode> getChildNodes() {
            return childNodes;
        }

        public MethodNode getParentNode() {
            return parentNode;
        }

        public void setParentNode(MethodNode parentNode) {
            this.parentNode = parentNode;
        }
    }
}
