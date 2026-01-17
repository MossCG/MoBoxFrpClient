package org.moboxlab.MoBoxFrpClient.Tick;

import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Cache.CacheProcess;
import org.moboxlab.MoBoxFrpClient.Task.TaskTunnelStop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TickWatchdog {
    public static void runTick() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        String name = "tickThread-Watchdog";
        Thread thread = new Thread(TickWatchdog::tickThread);
        thread.setName(name);
        singleThreadExecutor.execute(thread);
        BasicInfo.logger.sendInfo("已启动Tick线程："+name);
    }

    @SuppressWarnings("BusyWait")
    private static void tickThread() {
        while (true) {
            try {
                List<String> errorProcess = new ArrayList<>();
                CacheProcess.processMap.forEach((id,process) -> {
                    if (!process.process.isAlive()) errorProcess.add(id);
                });
                errorProcess.forEach(id -> {
                    TaskTunnelStop.executeTask(id);
                    BasicInfo.logger.sendWarn("检测到隧道"+id+"运行异常，已自动停止！");
                });
                Thread.sleep(30000L);
            } catch (Exception e) {
                BasicInfo.logger.sendException(e);
                BasicInfo.logger.sendWarn("Tick线程"+Thread.currentThread().getName()+"执行时出现错误！");
            }
        }
    }
}
