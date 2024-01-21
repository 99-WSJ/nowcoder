package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String salt;           // 盐值
    private String email;
    private int type;              // 0-普通用户; 1-超级管理员; 2-版主;
    private int status;            // 0-未激活; 1-已激活;
    private String activationCode; // 激活码
    private String headerUrl;      // 头像的url
    private Date createTime;
}
