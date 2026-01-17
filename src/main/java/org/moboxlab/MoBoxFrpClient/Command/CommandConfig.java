package org.moboxlab.MoBoxFrpClient.Command;

import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Cache.CacheConfig;
import org.mossmc.mosscg.MossLib.File.FileCheck;
import org.mossmc.mosscg.MossLib.Object.ObjectCommand;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

import java.util.ArrayList;
import java.util.List;

public class CommandConfig extends ObjectCommand {
    @Override
    public List<String> prefix() {
        List<String> prefixList = new ArrayList<>();
        prefixList.add("config");
        return prefixList;
    }

    @Override
    public boolean execute(String[] args, ObjectLogger logger) {
        if (args.length < 2) {
            BasicInfo.logger.sendWarn("指令格式：config <list/example> ");
            return false;
        }
        switch (args[1]) {
            case "list":
                BasicInfo.logger.sendInfo("==================MoBoxFrp================");
                BasicInfo.logger.sendInfo("配置文件列表：");
                CacheConfig.configCache.forEach((key,data) -> {
                    BasicInfo.logger.sendInfo(key + " - "+data.getString("name"));
                });
                BasicInfo.logger.sendInfo("配置文件保存于./MoBoxFrp/configs");
                BasicInfo.logger.sendInfo("如需更新保存的配置文件，请使用reload指令！");
                break;
            case "example":
                FileCheck.checkFileExist("./MoBoxFrp/configs/example.json","frp/example.json");
                BasicInfo.logger.sendInfo("示例配置文件已保存于./MoBoxFrp/configs/example.json");
                BasicInfo.logger.sendInfo("如果此处已存在example.json，则示例配置文件将不会保存！");
                break;
            default:
                BasicInfo.logger.sendWarn("未知的指令，请使用help命令查询帮助！ ");
                break;
        }
        return true;
    }
}
