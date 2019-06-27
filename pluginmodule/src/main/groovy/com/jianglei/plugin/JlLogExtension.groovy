package com.jianglei.plugin

class JlLogExtension implements Serializable {
    /**
     * 是否追踪第三方依赖的方法执行数据
     */
    boolean traceThirdLibrary = false

    public JlLogExtension() {

    }

    public JlLogExtension(JlLogExtension extension) {
        this.traceThirdLibrary = extension.traceThirdLibrary
    }


    boolean getTraceThirdLibrary() {
        return traceThirdLibrary
    }

    void setTraceThirdLibrary(boolean traceThirdLibrary) {
        this.traceThirdLibrary = traceThirdLibrary
    }
}