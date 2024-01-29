package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


@Controller
public class LoginController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 注册页面
     * @return
     */
    @GetMapping("/register")
    public String getRegisterPage() {
        return "/site/register";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/site/login";
    }

    @PostMapping("/register")
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty()) {  // 注册成功
            model.addAttribute("msg", "注册成功，我们已经向您的邮箱发送了一封激活邮件，请尽快激活！");
            model.addAttribute("target", "/index");   // 跳转到首页
            return "/site/operate-result";                  // 返回结果页面
        } else {                            // 注册失败
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    /**
     * 激活链接: http://localhost:8080/community/activation/101/code
     * @param model
     * @param userId
     * @param code
     * @return
     */
    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model,
                             @PathVariable("userId") int userId,
                             @PathVariable("code") String code){
        int result = userService.activation(userId, code);
        if(result == ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功，你的账号可以使用了");
            model.addAttribute("target","/login");
        }else if(result == ACTIVATION_REPEAT){
            model.addAttribute("msg","无效操作，该账号已经激活过了");
            model.addAttribute("target","/index");
        }else{
            model.addAttribute("msg","激活失败，该激活码错误");
            model.addAttribute("target","/index");
        }
        return  "/site/operate-result"; // 最终访问的页面 site/operation-result.html
    }

    /**
     * 获取验证码
     * @param response 通过这个对象将图片输出给浏览器
     * @param session 用来存储验证码，之后验证使用
     */
    @RequestMapping(path="/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        // 将验证码存入session
        session.setAttribute("kaptcha",text);
        // 验证码的归属
//        String kapatchaOwner = CommunityUtil.generateUUID();
//        Cookie cookie = new Cookie("kaptchaOwner", kapatchaOwner)
//        response.addCookie(cookie);

        // 返回的类型
        response.setContentType("image/png");
        try {
            // 将图片以输出流的形式输出给浏览器
            OutputStream os=response.getOutputStream();
            ImageIO.write(image,"png",os);
        }catch (IOException e){
            logger.error("响应验证码失败："+e.getMessage());
        }

    }

    @PostMapping("/login")
    public String login(String username,String password,String code,boolean rememberMe,
                        Model model,HttpSession session,HttpServletResponse response){
        // 先判断验证码是否正确
        String kaptcha = (String)session.getAttribute("kaptcha");
        // kaptcha.equalsIgnoreCase 是否一致，忽略大小写
        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确");
            return "/site/login";
        }
        // 检查账号,密码
        int expiredSeconds = rememberMe ? REMEMBER_EXPIRED_SECOND:DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if(map.containsKey("ticket")){
            Cookie cookie =new Cookie("ticket",map.get("ticket").toString());
            // cookie 整个目录都有效
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            // 发送给页面
            response.addCookie(cookie);
            // 重定向到首页
            return "redirect:/index";
        }else{
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            // 依旧是在登录页面
            return "/site/login";
        }
    }

    /**
     * 退出
     * @param ticket
     */
    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login";
    }

}
