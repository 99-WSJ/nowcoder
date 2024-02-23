package com.nowcoder.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Aspect
public class ServiceLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

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

    @Before("pointcut()")
    public void before(JoinPoint joinPoint){
        // 用户[1.2.3.4]，在[xxx]时间，访问了[com.nowcoder.community.service.xxx()]。
        ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteHost();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // joinPoint.getSignature().getDeclaringTypeName() 获取类名
        // joinPoint.getSignature().getName() 获取方法名
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();

        logger.info(String.format("用户[%s],在[%s],访问了[%s]",ip,now,target));
    }
}
