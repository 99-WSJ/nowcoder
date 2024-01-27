import com.nowcoder.community.communityApplication;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes =  communityApplication.class)
public class mailTest {
    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("2137746787@qq.com", "test", "welcome hello");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "sun");
        String content= templateEngine.process("/mail/demo", context);
        System.out.println(context);
        mailClient.sendMail("2137746787@qq.com", "html", content);
    }

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testLoginTicketMapper(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(111);
        loginTicket.setTicket("dfs");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
        loginTicketMapper.insertLoginTicket(loginTicket);

        loginTicket= loginTicketMapper.selectByTicket("dfs");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("dfs",1);
        loginTicket= loginTicketMapper.selectByTicket("dfs");
        System.out.println(loginTicket);
    }

}
