package org.moboxlab.MoBoxFrpClient.Command;

import org.moboxlab.MoBoxFrpClient.Main;
import org.mossmc.mosscg.MossLib.Object.ObjectCommand;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

import java.util.ArrayList;
import java.util.List;

public class CommandReload extends ObjectCommand {
    @Override
    public List<String> prefix() {
        List<String> prefixList = new ArrayList<>();
        prefixList.add("reload");
        return prefixList;
    }

    @Override
    public boolean execute(String[] args, ObjectLogger logger) {
        Main.reload();
        return true;
    }
}
