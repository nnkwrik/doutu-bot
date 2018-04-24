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
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
