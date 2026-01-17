package org.moboxlab.MoBoxFrpClient.Task;

import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.mossmc.mosscg.MossLib.File.FileCheck;

import java.lang.management.ManagementFactory;

public class TaskSetHook {
    public static void executeTask(){
        try {
            //设置正常退出Hook
            Runtime.getRuntime().addShutdownHook(new Thread(TaskShutdown::executeTask));
            //获取PID
            String[] runtimeName = ManagementFactory.getRuntimeMXBean().getName().split("@");
            BasicInfo.logger.sendInfo("进程数字ID："+runtimeName[0]);
            BasicInfo.logger.sendInfo("进程执行者ID："+runtimeName[1]);
            //启动守护进程
            FileCheck.checkFileExist("./MoBoxFrp/MoBoxFrpDaemon.jar","MoBoxFrpDaemon.jar");
            String command = "java -jar -server -Xmx100M ./MoBoxFrp/MoBoxFrpDaemon.jar";
            command += (" -systemType="+BasicInfo.config.getString("systemType"));
            command += (" -pid="+runtimeName[0]);
            BasicInfo.daemon = Runtime.getRuntime().exec(command);
            BasicInfo.logger.sendInfo(command);
            BasicInfo.logger.sendInfo("守护进程已启动！");
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendWarn("执行任务SetHook时出现异常！");
        }
    }

}
