package org.moboxlab.MoBoxFrpClient.Tick;

import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Task.TaskStatus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TickStatus {
    public static void runTick() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        String name = "tickThread-Status";
        Thread thread = new Thread(TickStatus::tickThread);
        thread.setName(name);
        singleThreadExecutor.execute(thread);
        BasicInfo.logger.sendInfo("已启动Tick线程："+name);
    }

    @SuppressWarnings("BusyWait")
    private static void tickThread() {
        while (true) {
            try {
                TaskStatus.executeTask();
                Thread.sleep(5000L);
            } catch (Exception e) {
                BasicInfo.logger.sendException(e);
                BasicInfo.logger.sendWarn("Tick线程"+Thread.currentThread().getName()+"执行时出现错误！");
            }
        }
    }
}
