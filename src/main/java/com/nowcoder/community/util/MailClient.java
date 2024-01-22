package com.nowcoder.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 * 发邮箱的客户端 工具类
 */
@Component
public class MailClient {
    // 需要记录日志
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    // 使用 Spring 提供的 JavaMailSenderImpl 来发送邮件
    @Autowired
    private JavaMailSender mailSender;

    // 将 username 和 password 注入
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送邮件
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendMail(String to, String subject, String content) {
        try {
            // 创建一个简单的邮件消息
            // 由于是简单的邮件消息，不需要使用 MimeMessage
            // SimpleMailMessage 是 Spring 提供的一个简单的邮件消息类
            // 通过它可以很方便地发送简单的邮件消息，包括 text、subject、from、to 等
            // 但是不能添加附件，也不能使用 html
            // 所以，如果需要添加附件或者使用 html，就需要使用 MimeMessage
            // MimeMessage 是 Spring 提供的一个复杂的邮件消息类
            // 通过它可以很方便地发送复杂的邮件消息，包括 text、subject、from、to、附件、html 等
            // MimeMessage 是 javax.mail 提供的一个复杂的邮件消息类
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from); // 发件人
            helper.setTo(to);     // 收件人
            helper.setSubject(subject);               // 主题
            helper.setText(content, true);      // 内容
            mailSender.send(helper.getMimeMessage()); // 发送
        } catch (Exception e) {
            logger.error("发送邮件失败: " + e.getMessage());
        }
    }
}
