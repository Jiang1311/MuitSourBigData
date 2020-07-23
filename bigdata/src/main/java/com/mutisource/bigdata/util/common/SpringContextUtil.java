package com.mutisource.bigdata.util.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Jeremy
 * @create 2020 07 23 10:39
 */

@Component
public class SpringContextUtil implements ApplicationContextAware {


    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法。设置上下文环境
     *
     * @param context  Spring应用上下文环境
     * @throws BeansException spring bean 异常
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
            SpringContextUtil.applicationContext = context;

    }

    /**
     *
     * @return 返回applicationContext
     */
    public static ApplicationContext getApplicationContext(){
        return  applicationContext;
    }


    /**
     * 获取对象
     *
     * @param name 根据bean name获取对象
     * @return Object
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return SpringContextUtil.applicationContext.getBean(name);
    }

}
