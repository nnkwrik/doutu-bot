package com.github.nnkwrik.doutuBot;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestJava {

    /**
     * 获取无后缀文件的文件类型
     */
    @Test
    public void testContentType(){
        Path path = Paths.get("/home/user/code/wechat-robot4doutu/assets/images/2018/04/23/3659716335796410254null");
        String type = null;
        try {
            type = Files.probeContentType(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] str = type.split("/");
        System.out.println("type is " + str[str.length-1]);

    }

    @Test
    public void testSubString(){
        String str = "http://emoji.qpic.cn/wx_emoji/bCypfSTlfOuzoB9jFrRBf5scjLOmTEfgcSxHerJCTg01l0YibjyIUUA/";
        System.out.println( str.substring(str.lastIndexOf("/")));

    }


}
