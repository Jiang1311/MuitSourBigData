//package com.mutisource.bigdata.util.accesslimit;
//
//import java.lang.annotation.*;
//
///**
// * <p>
// *   类描述信息 自定义注解
// *      eg：@AccessLimit(limit=5,time=1,userName="test")访问次数，用户时间默认1分钟5次
// * </p>
// *
// * @author Created by Jeremy
// * @date 2020/5/9 14:22
// */
//@Inherited
//@Documented
//@Target({ElementType.FIELD,ElementType.TYPE,ElementType.METHOD,ElementType.LOCAL_VARIABLE})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface AccessLimit {
//
//    int limit() default 3;
//
//    int time() default 60;
//
//    String userName() default "";
//
//
//
//}
