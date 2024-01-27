package com.nowcoder.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String salt;           // 盐值 -- 用于密码加密,就是在密码后面加上一段随机字符串
    private String email;
    private int type;              // 0-普通用户; 1-超级管理员; 2-版主;
    private int status;            // 0-未激活; 1-已激活;
    private String activationCode; // 激活码
    private String headerUrl;      // 头像的url
    private Date createTime;
}
