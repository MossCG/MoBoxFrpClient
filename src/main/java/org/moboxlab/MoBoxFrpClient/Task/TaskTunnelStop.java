package org.moboxlab.MoBoxFrpClient.Task;

import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Cache.CacheProcess;
import org.moboxlab.MoBoxFrpClient.Object.ObjectProcess;

public class TaskTunnelStop {
    public static void executeTask(String id){
        try {
            ObjectProcess process = CacheProcess.processMap.get(id);
            process.stop();
            CacheProcess.processMap.remove(id);
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendWarn("执行任务TunnelStop时出现异常！");
        }
    }
}
