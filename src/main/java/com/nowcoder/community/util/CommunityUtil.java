package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;

/**
 * 工具类
 */
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

    /**
     * 将map转换成json字符串
     * @param code  状态码
     * @param msg   提示信息
     * @param map   数据
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String,Object> map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map !=null){
            for(String key: map.keySet()){
                json.put(key,map.get(key));
            }
        }
        return json.toJSONString();
    }

    // 重载
    public static String getJSONString(int code,String msg){
        return getJSONString(code,msg,null);
    }

    public static String getJSONString(int code){
        return getJSONString(code,null,null);
    }
}
