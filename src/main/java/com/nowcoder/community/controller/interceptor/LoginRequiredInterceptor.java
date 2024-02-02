package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 对添加自定义的注解的方法进行拦截处理
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断拦截的是否是方法
        if(handler instanceof HandlerMethod){
            // 转型
            HandlerMethod handlerMethod =(HandlerMethod) handler;
            // 获取到拦截到的对象
            Method method = handlerMethod.getMethod();
            // 按照指定的类型获取注解
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            // 如果有这个注解，并且用户没有登录，就重定向到登录页面
            if(loginRequired!=null && hostHolder.getUser()==null){
                // 如果用户没有登录，就重定向到登录页面
                // request.getContextPath() 获取到项目路径
                response.sendRedirect(request.getContextPath()+"/login");
                return false;
            }
        }
        return true;
    }
}
