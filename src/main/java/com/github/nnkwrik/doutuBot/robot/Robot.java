package com.github.nnkwrik.doutuBot.robot;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public abstract class Robot {

    private String RobType;

    public String getRobType() {
        return RobType;
    }

    public Robot(String robType) {
        this.RobType = robType;
    }

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();


    public abstract String getResult(String question);
}
