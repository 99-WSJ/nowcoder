package com.nowcoder.community.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.commons.lang.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {
    private static final Logger logger=LoggerFactory.getLogger(SensitiveFilter.class);
    // 替换符
    private static final String REPLACEMENT = "***";
    // 根节点
    private TrieNode rootNode=new TrieNode();

    // 在容器初始化的时候调用，在服务启动的时候就开始初始化前缀树
    @PostConstruct
    public void init(){
        // 在运行之后，所以的配置文件都会加载到 target\classes 下面。因此我们在这里读取敏感词文件
       try(
               InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
               // 读取这个文件的流，然后转换成字符流，然后再转换成缓冲流
               BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream))
           ){
                String keyword;
                // 在读取的时候，一行一行的读取，如果读取到的是空，就说明读取完了
                while ((keyword=reader.readLine())!=null){
                    // 添加到前缀树
                    this.addKeyword(keyword);
                }
            }catch (Exception e){
                    logger.error("加载敏感词文件失败"+e.getMessage());
            }

    }

    /**
     * 将一个敏感词添加到前缀树中
     */
    private void addKeyword(String keyword){
        // 临时节点 指向 根节点
        TrieNode tempNode = rootNode;
        for(int i=0;i<keyword.length();i++){
            char c =keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode == null){
                // 初始化子节点
                subNode=new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            // 指向子节点，进入下一轮循环
            tempNode = subNode;
            // 设置结束标识
            if(i==keyword.length()-1){
                // 最后一个字符设为敏感词
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        // 指针1：指向树,默认指向根节点
        TrieNode tempNode = rootNode;
        // 指针2：指向字符串的开头
        int begin = 0;
        // 指针3：最后指向最后的结果
        int position = 0;
        StringBuilder sb=new StringBuilder();

        while(position<text.length()){
            // 取到当前位置的字符
            char c = text.charAt(position);
            // 跳过符号 *开*票*
            if(isSymbol(c)){
                // 若指针1处于根节点，将此符号计入结果，让指针2向下走一步
                if(tempNode == rootNode){
                    sb.append(c);
                    begin++;  // 指针2向下走一步
                }
                // * 开 * 票 *  ，当指针2 指向 '开' 时，此时指针 3指向 '*' ,此时无论指针2走不走，指针3都向下走一步
                // 无论符号在开头或者中间，指针3都向下走一步
                position++;
                // 当前的符号为*,跳过当前的符号，进入下一轮循环
                continue;
            }
            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            // 下级没有节点
            if(tempNode == null){
                // 以begin开头的字符串不是敏感词,将begin位置的字符计入结果sb，让指针2向下走一步
                sb.append(text.charAt(begin));
                // 指针2 后移一位，指针3再次指向指针2
                position = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
            }else if(tempNode.isKeywordEnd()){  // 发现敏感词
                // 发现敏感词，将begin~position字符串替换掉
                sb.append(REPLACEMENT);
                // 指针3向下走一步
                begin = ++position;
                // 重新指向根节点
                tempNode = rootNode;
            }else{
                // 当前指针3指向的位置不为空，同时也不是敏感词，此时直接检查下一个字符
                position++;
            }
        }
        // 此时退出循环了，但是此时 指针2 仍然没有走到最后，指针3到达了终点，因此将最后一批字符计入结果
        // 将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    private boolean isSymbol(Character c){
        // 0x2E80-0x9FFF 东亚文字范围
        // 如果返回为true，说明是一个符号
        return !CharUtils.isAsciiAlphanumeric(c) && (c<0x2E80 || c>0x9FFF);
    }
    /**
     * 前缀树
     * 内部类
     */
    private class TrieNode{
        // 关键词结束标识
        private boolean isKeywordEnd = false;
        // 子节点（key是下级字符，value是下级节点）
        private Map<Character,TrieNode> sunNodes =new HashMap<>();
        // 添加子节点的方法
        public void addSubNode(Character c,TrieNode node){
            sunNodes.put(c,node);
        }
        // 获取子节点的方法
        public TrieNode getSubNode(Character c){
            return sunNodes.get(c);
        }
        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }
    }

}
