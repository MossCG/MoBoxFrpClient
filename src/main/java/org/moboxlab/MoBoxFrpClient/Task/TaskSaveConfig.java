package org.moboxlab.MoBoxFrpClient.Task;

import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Cache.CacheConfig;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class TaskSaveConfig {
    public static void executeTask(String key){
        try {
            JSONObject data = CacheConfig.configCache.get(key);
            String configFile = "./MoBoxFrp/configs/"+key+".json";
            //写入配置文件
            BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
            writer.write(JSONObject.toJSONString(data,true));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendWarn("执行任务SaveConfig时出现异常！");
        }
    }
}
