package com.mutisource.bigdata.util.accesslimit;


import com.mutisource.bigdata.util.common.LogUtil;
import org.mp4parser.aspectj.lang.ProceedingJoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.lang.annotation.Around;
import org.mp4parser.aspectj.lang.annotation.Aspect;
import org.mp4parser.aspectj.lang.annotation.Pointcut;
import org.mp4parser.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 类描述信息 切面 控制访问频率
 * </p>
 *
 * @author Created by Jeremy
 * @date 2020/5/9 14:24
 */
@Component
@Scope
@Aspect
public class AccessLimitAspect {

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Resource
    private HttpServletResponse response;

    @Pointcut("@annotation(com.mutisource.bigdata.util.accesslimit.AccessLimit)")
    public void limitService() {

    }

    /**
     * 这里@Around("execution(* com.njnst.weather.*.*(..))")可以写具体的路径
     * 表示weather包下所有的方法都会调用这个方法@
     * 我们这边是Around("limitService()") 通过注解的方式 对添加该注解的方法进行调用
     *
     * @param joinPoint 连接点
     * @return object
     * @throws IOException e
     */
    @Around("limitService()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取拦截的方法相关信息
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        Object target = joinPoint.getTarget();
        // 为了获取注解信息
        Method currentMethod = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        //获取注解信息
        AccessLimit accessLimit = currentMethod.getAnnotation(AccessLimit.class);

        // 限流策略，根据package和方法名称组成Key + 用户userName
        String userName = accessLimit.userName();
        String packageName = (methodSignature.getMethod().getDeclaringClass()).getName();
        String functionKey = packageName + "_" + methodSignature.getName() + userName;

        // 固定时间
        int time = accessLimit.time();
        // 固定时间内最大次数
        int maxLimit = accessLimit.limit();
        Integer limit = redisTemplate.opsForValue().get(functionKey);
        if (limit == null) {
            // 用户首次访问将"key = 1"存入
            redisTemplate.opsForValue().set(functionKey, 1, time, TimeUnit.SECONDS);
            return joinPoint.proceed();
        } else if (limit <= maxLimit) {
            // 非首次访问value + 1；
            redisTemplate.opsForValue().set(functionKey, (limit + 1), time, TimeUnit.SECONDS);
            return joinPoint.proceed();
        } else {
            LogUtil.info("当前 {} 请求超出设定的访问次数，请稍后再试！" + functionKey);
            // 提示访问
            output(response, "当前请求超出设定的访问次数，请稍后再试！");
        }
        return null;
    }


    /**
     * 限制提示页面
     * @param response resp
     * @param msg 提示信息
     * @throws IOException
     */
    public void output(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }


}

