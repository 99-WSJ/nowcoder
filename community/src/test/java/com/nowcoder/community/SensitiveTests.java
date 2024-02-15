package com.nowcoder.community;

import com.nowcoder.community.utils.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wsj
 * @description
 * @date 2024年01月25日 22:50
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTests {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text = "这里可以赌博，吸毒哈哈哈哈";
        text = sensitiveFilter.filter(text);
        System.out.println(text);

        String text1 = "这里可以☆赌☆博☆，可以☆吸☆毒☆哈哈哈哈";
        text1 = sensitiveFilter.filter(text1);
        System.out.println(text1);

    }
}
