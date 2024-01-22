package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    private UserService userService;

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
//    @GetMapping("/activation")
//    public String activation(Model model,
//                             @RequestParam int userId,
//                             @RequestParam String code){
//        int result = userService.activation(userId, code);
//        if(result == ACTIVATION_SUCCESS){
//            model.addAttribute("msg","激活成功，你的账号可以使用了");
//            model.addAttribute("target","/login");
//        }else if(result == ACTIVATION_REPEAT){
//            model.addAttribute("msg","无效操作，该账号已经激活过了");
//            model.addAttribute("target","/login");
//        }else{
//            model.addAttribute("msg","激活失败，该激活码错误");
//            model.addAttribute("target","/login");
//        }
//        return  "/site/operate-result"; // 最终访问的页面 site/operation-result.html
//    }

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

}
