package com.github.nnkwrik.doutuBot;


import com.github.nnkwrik.doutuBot.handle.MessageHandler;

import com.github.nnkwrik.doutuBot.robot.RobotType;
import com.github.nnkwrik.doutuBot.utils.FileUtil;
import io.github.biezhi.wechat.api.constant.Config;


public class Main {

    public static void main(String[] args) {
        if(FileUtil.clearAllMultiFile()){
            Config config = Config.me().autoLogin(true).showTerminal(true);
            new MessageHandler(config ,RobotType.TULING).start();
        }
    }
}
