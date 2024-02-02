package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 评论的mapper接口
 */
@Mapper
public interface CommentMapper {
    /**
     * 根据id查询评论
     * @param entityType 评论类型,0-帖子的评论;1-评论的评论;2-课程的评论
     * @param entityId   评论的id
     * @param offset     分页的起始行
     * @param limit      每页显示的最大行数
     * @return
     */
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    /**
     * 查询评论的数量
     * @param entityType
     * @param entityId
     * @return
     */
    int SelectCountByEntity(int entityType, int entityId);
}
