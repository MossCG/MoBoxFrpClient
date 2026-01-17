package org.moboxlab.MoBoxFrpClient.Task;

import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Cache.CacheConfig;

import java.io.*;

public class TaskReadConfig {
    public static void executeTask(boolean autoStart){
        try {
            CacheConfig.configCache.clear();
            File file = new File("./MoBoxFrp/configs");
            if (!file.exists()) return;
            File[] list = file.listFiles();
            if (list != null) {
                for (File configFile : list) {
                    BufferedReader reader = new BufferedReader(new FileReader(configFile));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) builder.append(line);
                    JSONObject config = JSONObject.parseObject(builder.toString());
                    CacheConfig.configCache.put(configFile.getName().split("\\.")[0],config);
                    BasicInfo.logger.sendInfo("已加载保存的配置文件："+configFile.getName());
                }
            }
            if (autoStart) {
                CacheConfig.configCache.forEach((id,data) -> {
                    if (data.getBoolean("autoStart")) {
                        TaskTunnelStart.executeTask(id);
                        BasicInfo.logger.sendInfo("已自动启动配置文件隧道："+id);
                    }
                });
            }
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendWarn("执行任务ReadConfig时出现异常！");
        }
    }
}
