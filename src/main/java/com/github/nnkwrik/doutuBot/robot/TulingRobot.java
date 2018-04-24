package com.github.nnkwrik.doutuBot.robot;

import com.github.nnkwrik.doutuBot.model.Environment;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.github.nnkwrik.doutuBot.utils.Utils;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class TulingRobot extends Robot {


    private String baseUrl = "http://www.tuling123.com/openapi/api";
    private String apiKey;
    private String apiSecret;

    public TulingRobot(Environment environment) {
        super("TulingRobot");
        apiKey = environment.get("tuling.api_key");
        apiSecret = environment.get("tuling.api_secret");

    }


    @Override
    public String getResult(String question) {

        Map<String, Object> data = new HashMap<String, Object>(2);
        data.put("key", apiKey);
        data.put("info", question);

        //获取时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());
        //生成密钥
        String keyParam = apiSecret + timestamp + apiKey;
        String key = Md5.MD5(keyParam);

        //加密
        Aes mc = new Aes(key);
        String dataStr = mc.encrypt(Utils.toJson(data));

        //封装请求参数
        Map<String, Object> json = new HashMap<String, Object>(3);
        json.put("key", apiKey);
        json.put("timestamp", timestamp);
        json.put("data", dataStr);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Utils.toJson(json));
        Request request = new Request.Builder()
                .url(baseUrl)
                .post(requestBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            TulingRet tulingRet = Utils.fromJson(response.body().string(), TulingRet.class);
            if (tulingRet.code == 100000) {
                // 图灵机器人的第二人称是“小主人”，会影响HanPL的关键词提取
                return tulingRet.text.replaceAll("小主人","你");
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    class TulingRet {
        int code;
        String text;

    }



}
