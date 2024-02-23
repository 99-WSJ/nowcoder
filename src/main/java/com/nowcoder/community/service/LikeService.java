package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞
     * @param userId       当前的人
     * @param entityType   评论的类型
     * @param entityId     评论的id
     * @param entityUserId 评论的作者
     */
    public void like(int userId,int entityType, int entityId, int entityUserId){

//        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
//        Boolean member = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
//        if(member){
//            redisTemplate.opsForSet().remove(entityLikeKey, userId);
//        }else{
//            redisTemplate.opsForSet().add(entityLikeKey, userId);
//        }

        /**
         * 优化：使用事务
         * 1.当前的赞
         * 2.统计用户的赞
         */
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                // 看当前用户对这个实体是否点过赞
                boolean member = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
                // 开启事务
                operations.multi();
                // 如果点过赞，就取消赞
                if(member){
                    redisTemplate.opsForSet().remove(entityLikeKey, userId);
                    redisTemplate.opsForValue().decrement(userLikeKey);
                }else{
                    redisTemplate.opsForSet().add(entityLikeKey, userId);
                    redisTemplate.opsForValue().increment(userLikeKey);
                }
                // 执行事务
                return operations.exec();
            }
        });
    }

    // 查询某实体点赞的数量
    public long findEntityLikeCount(int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    // 查询某人对某实体的点赞状态 1:点赞 0:未点赞 -1 :踩
    public int findEntityLikeStatus(int userId, int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

    // 查询某个用户获得的赞的数量
    public int findUserLikeCount(int userId){
        // 拼出一个 key
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        // 根据用户的 key 查询出点赞的数量
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count.intValue();
    }

}
