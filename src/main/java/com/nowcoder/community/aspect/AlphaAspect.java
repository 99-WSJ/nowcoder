package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class AlphaAspect {

    /**
     *  * com.nowcoder.community.service.*.*(..))"
     *  * 表示返回值类型不限
     *  com.nowcoder.community.service.*.*(..)) 表示拦截com.nowcoder.community.service包下的所有类的所有方法
     *  .*.*(..)：第一个 * 表示类名，第二个 * 表示方法名，(..)表示方法的参数
     * 切点
     */
     @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
     public void pointcut(){

     }

     // 前置通知
    @Before("pointcut()")
    public void before(){
        System.out.println("before");
    }

    @After("pointcut()")
    public void after(){
        System.out.println("after");
    }

    @AfterReturning("pointcut()")
    public void afterReturning(){
        System.out.println("afterReturning");
    }

    // 抛异常时织入代码
    @AfterThrowing("pointcut()")
    public void afterThrowing(){
        System.out.println("afterThrowing");
    }

    /**
     * 前后都织入逻辑
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("around before");
        Object o = joinPoint.proceed();
        System.out.println("around after");
        return o;
    }


}
