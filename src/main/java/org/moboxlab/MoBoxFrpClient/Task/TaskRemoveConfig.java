package org.moboxlab.MoBoxFrpClient.Task;

import org.moboxlab.MoBoxFrpClient.BasicInfo;

import java.io.File;

public class TaskRemoveConfig {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void executeTask(String name){
        try {
            //文件路径
            String configFile = "./MoBoxFrp/frp/"+name+".toml";
            //删除配置文件
            File file = new File(configFile);
            file.delete();
            //删除frp本体
            String systemType = BasicInfo.config.getString("systemType");
            switch (systemType) {
                case "Windows":
                    file = new File("./MoBoxFrp/frp/frpc-"+name+".exe");
                    file.delete();
                    break;
                case "Linux":
                    file = new File("./MoBoxFrp/frp/frpc-"+name);
                    file.delete();
                    break;
                default:
                    BasicInfo.logger.sendWarn("不支持的平台类型: " + systemType);
                    break;
            }
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendWarn("执行任务RemoveConfig时出现异常！");
        }
    }
}
