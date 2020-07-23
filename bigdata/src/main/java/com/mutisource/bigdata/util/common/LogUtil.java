package com.mutisource.bigdata.util.common;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeremy
 * @create 2020 07 23 11:02
 */
@NoArgsConstructor
public class LogUtil {

    private static Logger logger = LoggerFactory.getLogger(LogUtil.class);


    /**
     * 错误
     * @param msg error-info
     */
    public static void error(String msg){
        logger.error(msg);
    }

    /**
     * 警告
     * @param msg warn-info
     */
    public static void warn(String msg){
        logger.warn(msg);
    }

    /**
     * 信息
     * @param msg info
     */
    public static void info(String msg){
        logger.info(msg);
    }

    /**
     * 调试
     * @param msg debug-info
     */
    public static void debug(String msg){
        logger.debug(msg);
    }
}
