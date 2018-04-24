package com.github.nnkwrik.doutuBot.emo;


import com.github.nnkwrik.doutuBot.emotion.WechatEmoAPI;


import io.github.biezhi.wechat.utils.WeChatUtils;
import org.junit.Test;

import java.io.File;


public class TestEmoAPI {

    @Test
    public void getEmoUrl() {
        // DoutulaAPI doutula = new DoutulaAPI(new WechatEmoAPI());
        // Doutula result = Utils.fromJson(doutula.getEmoByKeyword("额"), Doutula.class);
        // List<EmoInfo> list = result.getData().getList();
        // int i = WeChatUtils.random(0,list.size()-1);
        //     System.out.println(list.get(i).getImage_url()+i);

    }

    @Test
    public void sendDefaultEmo() {

        File[] emos = new File("assets" + "/" + WechatEmoAPI.DEFAULT_EMO_DIR).listFiles();
        System.out.println(emos[WeChatUtils.random(0,emos.length-1)].getPath());

    }
}
