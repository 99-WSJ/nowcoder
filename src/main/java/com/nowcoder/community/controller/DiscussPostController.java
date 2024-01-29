package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(path="/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title,String content){
        // 1.先检查是否登录
        User user = hostHolder.getUser();
        if(user == null){
            return CommunityUtil.getJSONString(403,"你还没有登录哦！");
        }
        // 2.添加帖子到数据库
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);
        // 3.返回结果,报错的情况下，将来统一进行处理
        return CommunityUtil.getJSONString(0,"发布成功！");
    }


    @RequestMapping(path="/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
        // 1.根据帖子的id查询帖子的详情
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        // 2.将帖子的详情传给前端
        model.addAttribute("post",post);
        // 3.根据帖子的作者的id查询帖子的作者
        User user = userService.findUserById(post.getUserId());
        // 4.将帖子的作者传给前端
        model.addAttribute("user",user);

        // 5.评论的分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/"+discussPostId);
        page.setRows(post.getCommentCount());

        // 6.查询帖子的评论
        // 评论 : 给帖子的评论
        // 回复 : 给评论的评论
        // 评论的列表
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        // 评论的VO列表
        List<Map<String,Object>> commentVoList = new ArrayList<>();
        if(commentList !=null){
            for(Comment comment:commentList){
                // 评论的VO
                Map<String,Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment",comment);
                // 评论的作者
                commentVo.put("user",userService.findUserById(comment.getUserId()));

                // 评论的回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复的VO列表
                List<Map<String,Object>> replyVoList = new ArrayList<>();
                if(replyList !=null){
                    for(Comment reply:replyList){
                        Map<String,Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply",reply);
                        // 回复的作者
                        replyVo.put("user",userService.findUserById(reply.getUserId()));
                        // 回复的目标
                        // 只出现在回复的回复中，即回复的回复才有目标
                        // 评论的回复不会有目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target",target);
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys",replyVoList);

                // 回复的数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount",replyCount);
                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments",commentVoList);
        // 5.返回帖子详情的页面
        return "/site/discuss-detail";
    }

}
