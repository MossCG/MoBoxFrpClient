package org.moboxlab.MoBoxFrpClient.Task;

import org.moboxlab.MoBoxFrpClient.BasicInfo;

import java.lang.management.ManagementFactory;

public class TaskSetHook {
    public static void executeTask(){
        try {
            //设置正常退出Hook
            Runtime.getRuntime().addShutdownHook(new Thread(TaskShutdown::executeTask));
            //设置外部Hook
            String[] runtimeName = ManagementFactory.getRuntimeMXBean().getName().split("@");
            BasicInfo.logger.sendInfo("进程数字ID："+runtimeName[0]);
            BasicInfo.logger.sendInfo("进程执行者ID："+runtimeName[1]);
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendWarn("执行任务SetHook时出现异常！");
        }
    }

}
