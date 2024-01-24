package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CookieUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * Cookie是通过request传过来的。所以我们可以把Cookie进行封装一下。
     * getValue就是从Cookie中取值。
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从cookie中取值
        String ticket = CookieUtil.getValue(request, "ticket");
        if(ticket!=null){
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            if(loginTicket !=null && loginTicket.getStatus()==0 && loginTicket.getExpired().after(new Date())){
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户
                // 持有用户就是对用户进行保存。服务器 在处理这个请求的时候是多线程环境，所有我们需要考虑线程的隔离。在多线程中有一个工具ThreadLocal.
                 hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        // 需要将user对象放到modelAndView中，所以需要判断modelAndView是否为空
        if(user != null && modelAndView!=null){
            modelAndView.addObject("loginUser",user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在模板引擎之后执行
        hostHolder.clear();
    }
}
