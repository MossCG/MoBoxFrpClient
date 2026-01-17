package org.moboxlab.MoBoxFrpClient.Task;

import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Cache.CacheConfig;
import org.moboxlab.MoBoxFrpClient.Cache.CacheProcess;
import org.moboxlab.MoBoxFrpClient.Object.ObjectProcess;

public class TaskTunnelStart {
    public static void executeTask(String id){
        try {
            ObjectProcess process = new ObjectProcess();
            process.name = id;
            process.data = CacheConfig.configCache.get(id);
            process.start();
            CacheProcess.processMap.put(id,process);
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendWarn("执行任务TunnelStart时出现异常！");
        }
    }
}
