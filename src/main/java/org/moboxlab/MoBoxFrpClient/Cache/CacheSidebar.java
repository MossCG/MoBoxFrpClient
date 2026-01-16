package org.moboxlab.MoBoxFrpClient.Cache;

import com.alibaba.fastjson.JSONArray;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.mossmc.mosscg.MossLib.File.FileCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class CacheSidebar {
    //缓存
    public static JSONArray sidebarCache = new JSONArray();

    public static void loadSidebar() {
        FileCheck.checkDirExist("./MoBoxFrp/pages");
        FileCheck.checkFileExist("./MoBoxFrp/pages/sidebar.json","pages/sidebar.json");
        loadFile();
    }

    private static void loadFile() {
        try {
            String fileName = "./MoBoxFrp/pages/sidebar.json";
            File file = new File(fileName);
            if (!file.exists()) return;
            InputStreamReader input = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(input);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) builder.append(line);
            sidebarCache = JSONArray.parseArray(builder.toString());
            BasicInfo.logger.sendInfo("已加载侧边栏文件："+fileName);
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
        }
    }
}
