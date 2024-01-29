package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

@Service
public class AlphaService {
    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 声明式业务
     * 管理事务
     * isolation = Isolation.READ_COMMITTED 事务的隔离级别
     * 传播机制 : 业务方法A调用业务方法B，如果A和B都有事务，那么B就会在A的事务中运行
     * propagation = Propagation.REQUIRED 支持当前事务（A调用B，如果A有事务，B就在A的事务中运行，如果A没有事务，B就开启一个新的事务）
     * REQUIRED : 支持当前事务(外部事务)，如果不存在就创建新事务
     * REQUIRES_NEW : 创建一个新事务，并且暂停当前事务(外部事务)
     * NESTED : 如果当前存在事务(外部事务)，则嵌套在该事务中执行(独立的提交和回滚)，否则就和REQUIRED一样
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public Object save1(){
        // 1. 新增用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.MD5("123" + user.getSalt()));
        user.setEmail("567@qq.com");
        user.setHeaderUrl("http://images.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        // 2. 新增帖子
        DiscussPost post = new DiscussPost();
        // 在 xml文件中 设置了 keyProperty="id" useGeneratedKeys="true"，所以插入后会自动生成id
        post.setUserId(user.getId());
        post.setTitle("Hello");
        post.setContent("新人报道！");
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        Integer.valueOf("abc");
        return userService.findUserById(101);
    }

    /**
     * 编程式事务管理
     */
    public Object save2(){
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        // 回调接口
        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                User user = new User();
                user.setUsername("bata");
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.MD5("123" + user.getSalt()));
                user.setEmail("bata@qq.com");
                user.setHeaderUrl("http://images.nowcoder.com/head/99t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);
                // 2. 新增帖子
                DiscussPost post = new DiscussPost();
                // 在 xml文件中 设置了 keyProperty="id" useGeneratedKeys="true"，所以插入后会自动生成id
                post.setUserId(user.getId());
                post.setTitle("Ho");
                post.setContent("新人！");
                post.setCreateTime(new Date());
                discussPostService.addDiscussPost(post);

                Integer.valueOf("abc");
                return "ok";
            }
        });
    }
}
