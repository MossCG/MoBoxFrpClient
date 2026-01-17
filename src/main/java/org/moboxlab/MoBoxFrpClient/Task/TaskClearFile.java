package org.moboxlab.MoBoxFrpClient.Task;

import org.moboxlab.MoBoxFrpClient.BasicInfo;

import java.io.File;

public class TaskClearFile {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void executeTask(){
        try {
            File file = new File("./MoBoxFrp/frp");
            if (!file.exists()) return;
            File[] list = file.listFiles();
            if (list != null) {
                for (File rubbish : list) {
                    if (!rubbish.getName().endsWith(".toml")) {
                        TaskKillProcess.executeTask(rubbish.getName());
                    }
                    rubbish.delete();
                }
            }
            file = new File("./MoBoxFrp/pages");
            if (!file.exists()) return;
            list = file.listFiles();
            if (list != null) {
                for (File rubbish : list) {
                    rubbish.delete();
                }
            }
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendWarn("执行任务UpdateFile时出现异常！");
        }
    }
}
