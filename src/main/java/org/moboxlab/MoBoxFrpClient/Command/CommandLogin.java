package org.moboxlab.MoBoxFrpClient.Command;

import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Task.TaskLogin;
import org.mossmc.mosscg.MossLib.Object.ObjectCommand;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

import java.util.ArrayList;
import java.util.List;

public class CommandLogin extends ObjectCommand {
    @Override
    public List<String> prefix() {
        List<String> prefixList = new ArrayList<>();
        prefixList.add("login");
        return prefixList;
    }

    @Override
    public boolean execute(String[] args, ObjectLogger logger) {
        if (args.length < 4) {
            BasicInfo.logger.sendWarn("指令格式：login <phone/email> <account> <password>");
            return false;
        }
        TaskLogin.executeTask(args[1],args[2],args[3]);
        return true;
    }
}
