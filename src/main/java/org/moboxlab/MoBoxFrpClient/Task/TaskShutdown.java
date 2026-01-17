package org.moboxlab.MoBoxFrpClient.Task;

import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Cache.CacheProcess;

public class TaskShutdown {
    public static void executeTask(){
        try {
            CacheProcess.processMap.forEach((key, process) -> process.asyncStop());
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendWarn("执行任务Shutdown时出现异常！");
        }
    }
}
