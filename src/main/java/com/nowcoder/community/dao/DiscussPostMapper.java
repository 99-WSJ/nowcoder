package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    /**
     *
     * @param userId  个人主页的帖子  动态的 sql语句 有时需要拼接 有的时候不需要拼接
     * @param offset  分页的起始行
     * @param limit   每页显示的最大行数
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     * 用于展示个人主页的帖子数量
     * @param userId 有的时候数据库的名字太长比较麻烦  可以用注解的方式取一个别名
     *               如果只有一个参数 并且在<if>里使用 一定要加别名
     * @return
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    /**
     * 添加帖子
     * @param discussPost  帖子
     * @return
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 根据帖子的id查询帖子的详情
     * @param id
     * @return
     */
    DiscussPost selectDiscussPostById(int id);

    /**
     * 更新帖子的数量
     * @param id
     * @param commentCount
     * @return
     */
    int updateCommentCount(int id,int commentCount);
}
