package com.nowcoder.community.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;

public class CommunityUtil {
    // 生成随机字符串
    public static String generateUUID() {
        return java.util.UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5 加密 密码加密 只能加密不能解密
    // hello -> abc123def456
    // hello + 3e4a8 -> abc123def456abc
    public static String MD5(String key) {
        // 判断是否为空
//        if (key == null || key.length() == 0) {
//            return null;
//        }
        if(StringUtils.isBlank(key)) {
            return null;
        }
        // 调用MD5加密算法
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
