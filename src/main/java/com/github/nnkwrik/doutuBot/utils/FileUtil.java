package com.github.nnkwrik.doutuBot.utils;

import com.github.nnkwrik.doutuBot.emotion.WechatEmoAPI;
import io.github.biezhi.wechat.api.constant.Config;
import io.github.biezhi.wechat.api.enums.ApiURL;
import io.github.biezhi.wechat.api.model.WeChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
    private FileUtil(){}
    /**
     * 清理assert文件夹中的所有多媒体缓存
     * @return
     */
    public static boolean clearAllMultiFile(){

        try {
            String assetsDir = new Config().assetsDir();
            deletefile(assetsDir + "/" +ApiURL.IMAGE.getDir());
            deletefile(assetsDir + "/" +ApiURL.VIDEO.getDir());
            deletefile(assetsDir + "/" +ApiURL.VOICE.getDir());
            deletefile(assetsDir + "/" +WechatEmoAPI.CHAT_EMO_DIR);
            deletefile(assetsDir + "/" +WechatEmoAPI.DOUTULA_EMO_DIR);
            log.info("文件缓存清理成功");
            return true;
        } catch (Exception e) {
            log.info("文件缓存清理失败");
            return false;
        }

    }

    /**
     * 删除该目录包括所有子目录及文件
     * @param delpath
     * @return
     * @throws Exception
     */
    private static boolean deletefile(String delpath) throws Exception {
        try {

            File file = new File(delpath);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "/" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                        // System.out.println(delfile.getAbsolutePath() + "删除文件成功");
                    } else if (delfile.isDirectory()) {
                        deletefile(delpath + "/" + filelist[i]);
                    }
                }
                // System.out.println(file.getAbsolutePath() + "删除成功");
                file.delete();
            }

        } catch (FileNotFoundException e) {
            System.out.println("deletefile() Exception:" + e.getMessage());
        }
        return true;
    }

    /**
     * 给从微信api下载的图片加后缀
     * @param message
     * @return
     */
    public static String correctImgName(WeChatMessage message){

        Path filePath = Paths.get(message.getImagePath());
        String fileType = null;
        try {
            fileType = Files.probeContentType(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] str = fileType.split("/");
        fileType = str[str.length-1];

        String correctImgName = filePath+"."+fileType;
        new File(message.getImagePath()).renameTo(new File(correctImgName));
        message.setImagePath(correctImgName);
        return correctImgName;
    }



}
