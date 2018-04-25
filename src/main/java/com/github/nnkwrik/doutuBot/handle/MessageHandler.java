package com.github.nnkwrik.doutuBot.handle;

import com.github.nnkwrik.doutuBot.emotion.DoutulaAPI;
import com.github.nnkwrik.doutuBot.emotion.WechatEmoAPI;
import com.github.nnkwrik.doutuBot.model.Environment;
import com.github.nnkwrik.doutuBot.robot.MoliRobot;
import com.github.nnkwrik.doutuBot.robot.Robot;
import com.github.nnkwrik.doutuBot.robot.RobotType;
import com.github.nnkwrik.doutuBot.robot.TulingRobot;
import com.github.nnkwrik.doutuBot.utils.FileUtil;
import com.hankcs.hanlp.HanLP;
import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.annotation.Bind;
import io.github.biezhi.wechat.api.constant.Config;
import io.github.biezhi.wechat.api.enums.AccountType;
import io.github.biezhi.wechat.api.enums.MsgType;
import io.github.biezhi.wechat.api.model.WeChatMessage;
import io.github.biezhi.wechat.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MessageHandler extends WeChatBot {
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
    private Robot robot;
    private WechatEmoAPI emoAPI;
    private DoutulaAPI doutulaAPI;


    public MessageHandler(Config config, RobotType robotType) {
        super(config);
        this.emoAPI = new WechatEmoAPI(this);
        this.doutulaAPI = new DoutulaAPI(emoAPI);

        Environment env = Environment.of("classpath:robot.properties");
        switch (robotType) {
            case TULING:
                robot = new TulingRobot(env);
                break;
            case MOLI:
                robot = new MoliRobot(env);
                break;
        }

    }


    @Bind(msgType = {MsgType.TEXT}, accountType = AccountType.TYPE_FRIEND)
    public void friendText(WeChatMessage message) {
        if (StringUtils.isEmpty(message.getFromUserName())) {
            return;
        }
        log.info("接收到好友 [{}] 的消息: {}", message.getName(), message.getText());
        if (message.getText().contains("自动回复") || message.getText().contains("机器人")) {
            log.info("被好友 [{}] 发现真相", message.getName());
            emoAPI.sendEmo(message.getFromUserName(), "assets/default-emos/YesNo/urright.gif");
            return;
        }
        String responseText = robot.getResult(message.getText());
        String keywords = "";
        for (String str : HanLP.extractKeyword(responseText, 3)) {
            keywords += str + "+";
        }
        keywords = keywords.substring(0, keywords.length() - 1);
        log.info("从" + robot.getRobType() + "的响应：\"{}\"中提取到关键字:【{}】 ", responseText, keywords);
        String dirPath = doutulaAPI.getEmoByKeyword(keywords);
        emoAPI.sendEmo(message.getFromUserName(), dirPath);
        log.info("根据关键字【{}】搜索表情,自动回复好友 [{}] ", keywords, message.getName());
    }


    @Bind(msgType = {MsgType.IMAGE}, accountType = AccountType.TYPE_FRIEND)
    public void friendImg(WeChatMessage message) {
        if (StringUtils.isEmpty(message.getFromUserName())) {
            return;
        }
        log.info("接收到好友 [{}] 发来的图片，已存于: {}", message.getName(), FileUtil.correctImgName(message));
        emoAPI.sendDefaultEmo(message.getFromUserName());
        log.info("自动回复好友 [{}] 默认表情", message.getName());
    }

    @Bind(msgType = {MsgType.EMOTICONS}, accountType = AccountType.TYPE_FRIEND)
    public void friendEmotion(WeChatMessage message) {
        if (StringUtils.isEmpty(message.getFromUserName())) {
            return;
        }
        message.setImagePath(emoAPI.downloadEmoUrl(message.getImagePath(), emoAPI.CHAT_EMO_DIR));
        log.info("接收到好友 [{}] 发来的表情，已存于: {}", message.getName(), FileUtil.correctImgName(message));
        emoAPI.sendDefaultEmo(message.getFromUserName());
        log.info("自动回复好友 [{}] 默认表情", message.getName());
    }


    @Bind(msgType = {MsgType.VIDEO, MsgType.VOICE}, accountType = AccountType.TYPE_FRIEND)
    public void friendUnsolve(WeChatMessage message) {
        if (StringUtils.isEmpty(message.getFromUserName())) {
            return;
        }
        log.info("好友 [{}] 发来的视频或语音，无法处理该类型",message.getName());
        emoAPI.sendDefaultEmo(message.getFromUserName());
        log.info("自动回复好友 [{}] 默认表情", message.getName());

    }


}
