import com.nowcoder.community.communityApplication;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
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


}
