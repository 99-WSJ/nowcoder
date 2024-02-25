package com.nowcoder.community;

import java.io.IOException;

public class WkTests {

    public static void main(String[] args) {
        String cmd = "G:/wkhtmltox/wkhtmltopdf/bin/wkhtmltoimage --quality 75  https://www.nowcoder.com F:/JAVA/JAVA_Program/niukeluntan/nowcoder/data/wk-images/3.png";
        try {
            Runtime.getRuntime().exec(cmd);
            // 先打印 ok； 等待操作系统执行命令 两个过程异步执行
            System.out.println("ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
