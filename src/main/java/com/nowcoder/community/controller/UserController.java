package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.nowcoder.community.util.CommunityConstant.ENTITY_TYPE_USER;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // 项目名
    @Value("${server.servlet.context-path}")
    private String contextPath;

    // 项目域名
    @Value("${community.path.domain}")
    private String domain;

    // 上传路径
    @Value("${community.path.upload}")
    private String uploadPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    /**
     * 个人设置
     * @LoginRequired 注解表示必须登录才能访问，自定义注解
     * @return
     */
    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage() {
        return "/site/setting";
    }

    /**
     * 上传头像,进行更新
     * @param headerImage
     * @param model
     * @return
     */
    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage== null) {
            model.addAttribute("error", "您还没有选择图片！");
            return "/site/setting";
        }
        // 文件名 查看 文件后缀
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件格式不正确！");
            return "/site/setting";
        }
        // 生成随机文件名
        fileName=CommunityUtil.generateUUID()+suffix;
        // 确定文件存放路径
        // D:/project/upload/xxx.png
        // 项目路径 + 上传路径 + 文件名
        File dest= new File(uploadPath + "/" + fileName);
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败："+e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常！",e);
        }
        // 当前路径的图像 更新到 数据库
        // web http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;

        userService.updateHeader(user.getId(),headerUrl);
        // 更新完成，回到首页
        return "redirect:/index";
    }

    /**
     * 获取头像
     * 是一个二进制的数据流，所以返回值是void，需要手动进行写入流
     * @param fileName
     * @param response
     */
    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        if(StringUtils.isBlank(fileName)){
            throw new IllegalArgumentException("文件名不能为空！");
        }
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        // 设置 响应的 Content-Type， 这里表示响应的内容是图片类型
        response.setContentType("image/"+suffix);
        // 使用字节流
        try(
                // 这个流需要自己来进行管理，需要手动关闭
                // 写在括号里面，最后会有一个 final 来进行关闭
                FileInputStream fileInputStream = new FileInputStream(fileName);
           ) {
            // 获取输出流对象，SpringMVC进行管理
            OutputStream outputStream = response.getOutputStream();
            // 输出文件
            // 创建文件的输入流,边读边输出
            // FileInputStream fileInputStream = new FileInputStream(fileName);
            // 建立一段缓存区，这么读是一段一段的读，效率更高
            byte[] buffer =new byte[1024];
            int b=0;
            // 等于 -1 的时候 读取完毕
            while((b = fileInputStream.read(buffer))!=-1){
                // 表示从 buffer的0索引开始读取，直至b索引
                outputStream.write(buffer,0,b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败："+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 查看个人主页
     * @param model
     * @param userId 传入的用户id，可以查看传入的id
     * @return
     */
    @RequestMapping(path="/profile/{userId}",method = RequestMethod.GET)
    public String getProfilePage(Model model, @PathVariable("userId") int userId){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("该用户不存在！");
        }
        // 用户
        model.addAttribute("user",user);
        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount",likeCount);

        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if(hostHolder.getUser() != null){
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }
}
