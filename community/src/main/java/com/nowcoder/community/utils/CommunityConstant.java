package com.nowcoder.community.utils;

public interface CommunityConstant {
    int ACTIVATION_SUCCESS=0;  // 激活成功
    int ACTIVATION_REPEAT=1;   // 重复激活
    int ACTIVATION_FAILURE=2;  // 激活失败
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;  //默认状态的登陆凭证的超时时间 12h
    int REMEMBERME_EXPIRED_SECONDS = 3600 * 24 * 10; //记录状态的登陆凭证超时时间 10天
}
