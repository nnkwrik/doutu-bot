package com.github.nnkwrik.doutuBot;

import com.github.nnkwrik.doutuBot.utils.FileUtil;
import io.github.biezhi.wechat.api.model.WeChatMessage;
import org.junit.Test;

public class TestUtil {

    /**
     * 给文件加上适当的后缀
     */
    @Test
    public void testChangePath(){

        WeChatMessage wechat =  new WeChatMessage();
        wechat.setImagePath("assets/images/2018/04/23/3659716335796410254null");
        // FileUtil.correctImgName(wechat);
    }

    /**
     * 清除项目本地的所有多媒体文件
     */
    @Test
    public void testClearAsset(){
        FileUtil.clearAllMultiFile();
    }
}
