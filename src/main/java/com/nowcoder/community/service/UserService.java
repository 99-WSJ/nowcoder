package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService  implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    // 邮件客户端
    @Autowired
    private MailClient mailClient;

    // 模板引擎
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    // 项目域名
    @Value("${community.path.domain}")
    private String domain;
    // 项目名
    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    /**
     * 注册方法
     * @param user
     * @return
     */
    public Map<String,Object> register(User user){
        Map<String,Object> map = new HashMap<>();
        // 空值处理
        if(user==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }
        // 验证账号
        User u = userMapper.selectByName(user.getUsername());
        if(u!=null){
            map.put("usernameMsg","该账号已存在");
            return map;
        }
        // 验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if(u!=null){
            map.put("emailMsg","该邮箱已被注册");
            return map;
        }
        // 注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.MD5(user.getPassword()+user.getSalt()));
        user.setType(0); // 普通用户
        user.setStatus(0); // 未激活
        user.setActivationCode(CommunityUtil.generateUUID()); // 激活码
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000))); // 随机头像
        user.setCreateTime(new Date());  // 注册时间
        userMapper.insertUser(user);     // 插入数据库


        // 发送激活邮件, 激活模板 + 激活链接 + 用户名
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        // 激活链接: http://localhost:8080/community/activation/101/code
        // todo 这里遇到了一个问题：mybatis-plus 没有处理自增主键的返回值，所以这里的user.getId()是 0
        String url = domain + contextPath + "/activation/" +  userMapper.selectByEmail(user.getEmail()).getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        // 激活模板
        String content = templateEngine.process("/mail/activation", context);
        // 发送邮件
        mailClient.sendMail(user.getEmail(), "激活账号", content);
        return map;
    }

    /**
     * 激活
     */
    public int activation(int userId,String code){
        User user = userMapper.selectById(userId);
        if(user.getStatus() == 1){  // 已经存在
            return ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(code)){  // 传递过来的激活码和数据中的激活码是一致的
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }else{
            return ACTIVATION_FAILURE;
        }
    }

    /**
     * 登录
     * @param username 账户
     * @param password 密码
     * @param expiredSeconds 过期时间
     * @return
     */
    public Map<String,Object> login(String username,String password,int expiredSeconds){
        Map<String,Object> map = new HashMap<>();

        // 空值处理
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空!");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空!");
            return map;
        }
        // 验证账号
        User user = userMapper.selectByName(username);
        if(user==null){
            map.put("usernameMsg","该账号不存在!");
            return map;
        }
        // 验证状态
        if(user.getStatus() == 0){
            map.put("usernameMsg","该账号未激活!");
            return map;
        }
        // 验证密码
        password = CommunityUtil.MD5(password + user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg","密码不正确");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        // 秒 换算成 毫秒
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds*1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket",loginTicket.getTicket());
        return map;
    }

}
