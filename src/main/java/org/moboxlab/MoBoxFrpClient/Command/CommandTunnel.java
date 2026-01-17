package org.moboxlab.MoBoxFrpClient.Command;

import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Cache.CacheConfig;
import org.moboxlab.MoBoxFrpClient.Cache.CacheProcess;
import org.moboxlab.MoBoxFrpClient.Task.TaskTunnelStart;
import org.moboxlab.MoBoxFrpClient.Task.TaskTunnelStop;
import org.mossmc.mosscg.MossLib.File.FileCheck;
import org.mossmc.mosscg.MossLib.Object.ObjectCommand;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

import java.util.ArrayList;
import java.util.List;

public class CommandTunnel extends ObjectCommand {
    @Override
    public List<String> prefix() {
        List<String> prefixList = new ArrayList<>();
        prefixList.add("tunnel");
        return prefixList;
    }

    @Override
    public boolean execute(String[] args, ObjectLogger logger) {
        if (args.length < 2) {
            BasicInfo.logger.sendWarn("指令格式：tunnel <list/start/stop> ");
            return false;
        }
        switch (args[1]) {
            case "list":
                BasicInfo.logger.sendInfo("==================MoBoxFrp================");
                BasicInfo.logger.sendInfo("隧道列表：");
                CacheProcess.processMap.forEach((key, process) -> {
                    BasicInfo.logger.sendInfo("["+process.data.getString("protocol")+"][" + key + "] - "+process.data.getString("name"));
                    String localAddress = process.data.getString("localAddress")+":"+process.data.getString("localPort");
                    String remoteAddress = process.data.getString("serverAddress")+":"+process.data.getString("openPort");
                    BasicInfo.logger.sendInfo("本地地址：" + localAddress);
                    BasicInfo.logger.sendInfo("远程地址：" + remoteAddress);
                });
                break;
            case "start":
                if (args.length < 3) {
                    BasicInfo.logger.sendWarn("指令格式：tunnel start <id> ");
                    BasicInfo.logger.sendWarn("注：id为配置文件的id哦~");
                    return false;
                }
                if (CacheProcess.processMap.containsKey(args[2])) {
                    BasicInfo.logger.sendWarn("注：这个隧道已经在运行了哦！");
                    return false;
                }
                TaskTunnelStart.executeTask(args[2]);
                break;
            case "stop":
                if (args.length < 3) {
                    BasicInfo.logger.sendWarn("指令格式：tunnel stop <id> ");
                    BasicInfo.logger.sendWarn("注：id为配置文件的id哦~");
                    return false;
                }
                if (!CacheProcess.processMap.containsKey(args[2])) {
                    BasicInfo.logger.sendWarn("注：这个隧道没有在运行哦！");
                    return false;
                }
                TaskTunnelStop.executeTask(args[2]);
                break;
            default:
                BasicInfo.logger.sendWarn("未知的指令，请使用help命令查询帮助！ ");
                break;
        }
        return true;
    }
}
