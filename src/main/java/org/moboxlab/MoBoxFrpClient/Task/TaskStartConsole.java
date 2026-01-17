package org.moboxlab.MoBoxFrpClient.Task;

import org.moboxlab.MoBoxFrpClient.BasicInfo;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class TaskStartConsole {
    public static void executeTask(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("run.bat"));
            writer.write("@echo off\r\n");
            writer.write("java -Xmx128m -server -jar MoBoxFrpClient.jar\r\n");
            writer.write("pause\r\n");
            writer.flush();
            writer.close();
            Runtime.getRuntime().exec("cmd /c start run.bat");
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendWarn("执行任务StartConsole时出现异常！");
        }
    }
}
