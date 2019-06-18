package com.jianglei.plugin

import org.slf4j.LoggerFactory

class LogUtils {
    private static logger = LoggerFactory.getLogger("MethodTrace")

    def static i(String s) {
        logger.warn(s)
    }
    def static e(String s){
        logger.error(s)
    }

}