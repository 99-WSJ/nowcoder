import com.nowcoder.community.communityApplication;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = communityApplication.class)
public class communityTest {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testDiscussPostMapper(){
        log.info("testDiscussPostMapper");
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10);
           for (DiscussPost discussPost : list) {
                log.info(discussPost.toString());
            }

        int rows = discussPostMapper.selectDiscussPostRows(0);
        log.info(String.valueOf(rows));
    }

    @Test
    public void testMessageMapper(){
        log.info("testMessageMapper");
        List<Message> list = messageMapper.selectConversations(111, 0, 20);
        for (Message message : list) {
            log.info(message.toString());
        }
        int count = messageMapper.selectConversationCount(111);
        log.info(String.valueOf(count));

        list = messageMapper.selectLetters("111_112", 0, 10);
        for (Message message : list) {
            log.info(message.toString());
        }
        count = messageMapper.selectLetterCount("111_112");
        log.info(String.valueOf(count));

        count = messageMapper.selectLetterUnreadCount(131, "111_131");
        log.info(String.valueOf(count));
    }


}
