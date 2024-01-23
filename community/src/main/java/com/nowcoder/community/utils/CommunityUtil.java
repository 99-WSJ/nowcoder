package com.nowcoder.community.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author wsj
 * @description
 * @date 2024年01月22日 22:42
 */
public class CommunityUtil {
    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("~", "");
    }
    //MD5加密
    //只加密。不解密 Hello ->> abc123def456
    //hello + 3e4as --> abc123def456abc
    public static String md5(String key) {
        if(StringUtils.isBlank(key)) {
            return null;
        }
        // 将输入的字符串转换为字节数组，计算 MD5 散列值
        // 将计算得到的 MD5 散列值转换为十六进制表示
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
