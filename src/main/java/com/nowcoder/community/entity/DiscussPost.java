package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * 帖子实体类
 */
@Data
public class DiscussPost {
    private int id;
    private int userId;
    private String title;  // title of the post
    private String content; // content of the post
    private int type; // 0: normal; 1: top(置顶)
    private int status; // 0: normal; 1: deleted(拉黑)
    private Date createTime;
    private int commentCount; // number of comments
    private double score; // score of the post, used for ranking

    @Override
    public String toString() {
        return "DiscussPost{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createTime=" + createTime +
                ", commentCount=" + commentCount +
                ", score=" + score +
                '}';
    }
}
