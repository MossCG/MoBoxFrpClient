package org.moboxlab.MoBoxFrpClient;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.mossmc.mosscg.MossLib.Object.ObjectConfig;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

public class BasicInfo {
    //版本信息，请勿修改
    //版本命名规则：V大版本.大更新.小更新.修改次数.时间戳 版本类型
    //大版本中 0为开发中版本，1为正式版本
    //大更新之间基本上互不兼容
    //小更新之间基本上只是小功能更新，可以上下兼容
    //修改次数只代表一些微调（但是基本上建议更新）
    //版本类型 Beta为测试版，Stable为稳定版
    public static String version = "V1.0.0.0.0309";
    public static String versionType = "Beta";

    //作者信息，请勿修改
    public static String author = "墨守MossCG";

    //贡献者信息，如果你做出了涉及代码的pr且代码量不少于100lines，请自行添加你的ID到此处
    public static String contributor = "MossCG";

    //API地址
    public static String api = "https://www.moboxfrp.top";
    //登录信息
    public static boolean login = false;
    public static String token = "";
    //用户信息
    public static JSONObject userInfo;
    public static JSONArray codeInfo;

    //守护进程
    public static Process daemon;

    //系统状态信息
    public static double memoryMax = 4.0;
    public static double memoryUsage = 2.0;
    public static double cpuUsage = 1.0;
    public static double bandUpload = 1.0;
    public static double bandDownload = 1.0;
    public static double bandUploadTotal = 1.0;
    public static double bandDownloadTotal = 1.0;

    //MossLib框架功能模块
    public static ObjectLogger logger;
    public static ObjectConfig config;

    //Debug信息输出
    public static boolean debug = false;
    public static void sendDebug(String message) {
        logger.sendAPI(message,debug);
    }
}
