package org.moboxlab.MoBoxFrpClient.Command;

import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.mossmc.mosscg.MossLib.Object.ObjectCommand;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

import java.util.ArrayList;
import java.util.List;

public class CommandHelp extends ObjectCommand {
    @Override
    public List<String> prefix() {
        List<String> prefixList = new ArrayList<>();
        prefixList.add("help");
        return prefixList;
    }

    @Override
    public boolean execute(String[] args, ObjectLogger logger) {
        BasicInfo.logger.sendInfo("==================MoBoxFrp================");
        BasicInfo.logger.sendInfo("登录：login <phone/email> <account> <password>");
        BasicInfo.logger.sendInfo("配置列表：config list");
        BasicInfo.logger.sendInfo("配置示例：config example");
        BasicInfo.logger.sendInfo("隧道列表：tunnel list");
        BasicInfo.logger.sendInfo("隧道启动：tunnel start <id>");
        BasicInfo.logger.sendInfo("隧道停止：tunnel stop <id>");
        BasicInfo.logger.sendInfo("重载配置：reload");
        BasicInfo.logger.sendInfo("退出客户端：exit");
        return true;
    }
}
