package com.github.nnkwrik.doutuBot.emotion;

import io.github.biezhi.wechat.utils.WeChatUtils;
import com.github.nnkwrik.doutuBot.model.Doutula;
import com.github.nnkwrik.doutuBot.model.EmoInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.github.nnkwrik.doutuBot.utils.Utils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DoutulaAPI {

    private String baseUrl = "https://www.doutula.com/api/search?keyword=";
    private WechatEmoAPI emoAPI;



    public DoutulaAPI(WechatEmoAPI emoAPI) {
        this.emoAPI = emoAPI;
    }

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public String getEmoByKeyword(String keyword) {
        Request request = new Request.Builder().url(baseUrl + keyword).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            Doutula resultJson = Utils.fromJson(response.body().string(), Doutula.class);
            List<EmoInfo> infoList = resultJson.getData().getList();
            String emoUrl;
            while (true) {
                //靠前的比较准?
                emoUrl = infoList.get(WeChatUtils.random(0, 10)).getImage_url();
                if (emoUrl.startsWith("https:")) {
                    break;
                }

            }

            return emoAPI.downloadEmoUrl(emoUrl, WechatEmoAPI.DOUTULA_EMO_DIR);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
