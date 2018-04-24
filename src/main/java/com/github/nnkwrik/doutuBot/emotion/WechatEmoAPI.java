package com.github.nnkwrik.doutuBot.emotion;

import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.request.FileRequest;
import io.github.biezhi.wechat.api.request.JsonRequest;
import io.github.biezhi.wechat.api.response.FileResponse;
import io.github.biezhi.wechat.api.response.JsonResponse;
import io.github.biezhi.wechat.utils.StringUtils;
import io.github.biezhi.wechat.utils.WeChatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WechatEmoAPI {

    private static final Logger log = LoggerFactory.getLogger(WechatEmoAPI.class);
    public static final String CHAT_EMO_DIR = "emos";
    public static final String DOUTULA_EMO_DIR = "doutula-emos";
    public static final String DEFAULT_EMO_DIR = "default-emos";
    private WeChatBot bot;

    public WechatEmoAPI(WeChatBot bot) {
        this.bot = bot;
    }

    public String downloadEmoUrl(String emoUrl, String dir) {

        String[] emoName = emoUrl.split("/");
        String dirPath = bot.config().assetsDir() + "/" + dir;

        FileResponse response = (FileResponse) bot.client().download(new FileRequest(emoUrl));
        InputStream inputStream = response.getInputStream();
        return WeChatUtils.saveFileByDay(inputStream, dirPath, emoName[emoName.length - 1], true).getPath();

    }

    public boolean sendEmo(String toUserName, String filePath) {
        // DateUtils.sendSleep();
        String mediaId = bot.api().uploadMedia(toUserName, filePath).getMediaId();
        if (StringUtils.isEmpty(mediaId)) {
            log.warn("Media为空");
            return false;
        }

        String url = String.format("%s/webwxsendemoticon?fun=sys&f=json&pass_ticket=%s",
                bot.session().getUrl(), bot.session().getPassTicket());

        String msgId = System.currentTimeMillis() / 1000 + StringUtils.random(6);

        Map<String, Object> msg = new HashMap<>();
        msg.put("Type", 47);
        msg.put("EmojiFlag", 2);
        msg.put("MediaId", mediaId);
        msg.put("FromUserName", bot.session().getUserName());
        msg.put("ToUserName", toUserName);
        msg.put("LocalID", msgId);
        msg.put("ClientMsgId", msgId);

        JsonResponse response = bot.client().send(new JsonRequest(url).post().jsonBody()
                .add("BaseRequest", bot.session().getBaseRequest())
                .add("Msg", msg)
        );
        return null != response && response.success();
    }

    public boolean sendDefaultEmo(String toUserName) {
        File[] emos = new File(bot.config().assetsDir() + "/" + DEFAULT_EMO_DIR).listFiles();
        return sendEmo(toUserName, emos[WeChatUtils.random(0, emos.length - 1)].getPath());
    }


}
