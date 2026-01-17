package org.moboxlab.MoBoxFrpClient.Task;

import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Cache.CacheConfig;

import java.io.File;

public class TaskDeleteConfig {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void executeTask(String key){
        try {
            //删除配置文件
            CacheConfig.configCache.remove(key);
            String configFile = "./MoBoxFrp/configs/"+key+".json";
            File file = new File(configFile);
            if (file.exists()) file.delete();
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendWarn("执行任务DeleteConfig时出现异常！");
        }
    }
}
