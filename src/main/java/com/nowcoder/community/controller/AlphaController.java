package com.nowcoder.community.controller;

import com.nowcoder.community.util.CommunityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class AlphaController {

    // cookie 示例
    @RequestMapping(path="/cookie/set",method= RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        // 创建 cookie
        Cookie cookie =new Cookie("code", CommunityUtil.generateUUID()); // cookie 名字和值
        cookie.setPath("/community/alpha"); // 设置 cookie 的生效范围
        cookie.setMaxAge(60*10); // 设置 cookie 的生存时间
        response.addCookie(cookie);
        return "cookie";
    }

    // session 示例
    @RequestMapping(path="/session/set",method= RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("id","1");
        session.setAttribute("name","test");
        return "session";
    }

    @RequestMapping(path="/session/get",method= RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "session";
    }

    // ajax 示例
    @RequestMapping(path="/ajax",method= RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0,"操作成功");
    }


}
