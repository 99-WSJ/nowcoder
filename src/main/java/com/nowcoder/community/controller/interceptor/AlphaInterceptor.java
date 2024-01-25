package com.nowcoder.community.controller.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */
@Component
public class AlphaInterceptor implements HandlerInterceptor {

    private static final Logger logger= LoggerFactory.getLogger(AlphaInterceptor.class);
    /**
     * 在 controller处理处理请求之前执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("preHandle:"+handler.toString());
         return true;
    }

    /**
     * 在 controller处理完请求之后执行,模板引擎之前运行
     * @param request
     * @param response
     * @param handler
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("postHandle:"+handler.toString());
    }

    /**
     * 在模板引擎之后运行
     * @param request
     * @param response
     * @param handler
     * @param ex       异常信息
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.debug("afterCompletion"+handler.toString());
    }
}
