import com.nowcoder.community.communityApplication;
import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
}
