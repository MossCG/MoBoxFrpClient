package org.moboxlab.MoBoxFrpClient.Tick;

import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Task.TaskGetCodes;
import org.moboxlab.MoBoxFrpClient.Task.TaskGetUserInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TickInfoUpdate {
    public static void runTick() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        String name = "tickThread-InfoUpdate";
        Thread thread = new Thread(TickInfoUpdate::tickThread);
        thread.setName(name);
        singleThreadExecutor.execute(thread);
        BasicInfo.logger.sendInfo("已启动Tick线程："+name);
    }

    @SuppressWarnings("BusyWait")
    private static void tickThread() {
        while (true) {
            try {
                Thread.sleep(30000L);
                //登录检查
                if (!BasicInfo.login) continue;
                //用户信息有效期60s
                TaskGetUserInfo.executeTask(false);
                //穿透码信息有效期45s
                TaskGetCodes.executeTask(false);
            } catch (Exception e) {
                BasicInfo.logger.sendException(e);
                BasicInfo.logger.sendWarn("Tick线程"+Thread.currentThread().getName()+"执行时出现错误！");
            }
        }
    }
}
