package com.github.nnkwrik.doutuBot.robot;

import com.github.nnkwrik.doutuBot.model.Environment;
import okhttp3.Request;
import okhttp3.Response;
import com.github.nnkwrik.doutuBot.utils.Utils;

import java.io.IOException;

public class MoliRobot extends Robot {


    private String baseUrl = "http://i.itpk.cn/api.php";


    public MoliRobot(Environment environment) {
        super("MoliRobot");
        String apiKey = environment.get("moli.api_key");
        String apiSecret = environment.get("moli.api_secret");
        if (Utils.isNotBlank(apiKey) && Utils.isNotBlank(apiSecret)) {
            baseUrl += "?api_key=" + apiKey + "&api_secret=" + apiSecret + "&";
        } else {
            baseUrl += "?";
        }
    }


    @Override
    public String getResult(String question) {
        String url = baseUrl + "question=" + question;
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String responseText = response.body().string();

            //没有设置Moli的api_key时,称呼会被代码代替,如 "[name]你好，我在发呆呢！"
            responseText = responseText.replaceAll("\\[cqname\\]", "我");
            responseText = responseText.replaceAll("\\[name\\]", "你");
            responseText = responseText.replaceAll("\\[sex\\]", "女");
            return responseText;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
