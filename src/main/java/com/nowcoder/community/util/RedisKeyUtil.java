package com.nowcoder.community.util;


public class RedisKeyUtil {
    private static final String SPLIT = ":";
    // 点赞
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    // 点赞用户
    private static final String PREFIX_USER_LIKE = "like:user";

    // 关注
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final  String PREFIX_FOLLOWER = "follower";

    // 登录验证码
    private static final String PREFIX_KAPTCHA = "kaptcha";

    // 登录凭证
    private static final String PREFIX_TICKET = "ticket";

    // 用户
    private static final String PREFIX_USER = "user";

    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户的赞
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    // 某个用户关注的实体
    // followee:userId:entityType  -> zset(entityId,nowTime)
    public static String getFolloweeKey(int userId,int entityType){
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个用户拥有的粉丝
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType,int entityId){
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    // 登录验证码
    // owner 是一个随机生成的字符串，作为一个临时的凭证。
    // 因为此时还没有用户的信息，所以需要一个随机的字符串来标识这个凭证。
    public static String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    // 登录凭证
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // 用户
    public static String getUserKey(int userId){
        return PREFIX_USER + SPLIT + userId;
    }
}
