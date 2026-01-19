package org.moboxlab.MoBoxFrpClient;

import org.moboxlab.MoBoxFrpClient.Command.*;
import org.moboxlab.MoBoxFrpClient.Task.*;
import org.moboxlab.MoBoxFrpClient.Tick.TickStatus;
import org.moboxlab.MoBoxFrpClient.Tick.TickWatchdog;
import org.moboxlab.MoBoxFrpClient.Web.WebMain;
import org.mossmc.mosscg.MossLib.Command.CommandManager;
import org.mossmc.mosscg.MossLib.Config.ConfigManager;
import org.mossmc.mosscg.MossLib.File.FileCheck;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        //计时
        long startTime = System.currentTimeMillis();

        //日志模块初始化
        FileCheck.checkDirExist("./MoBoxFrp");
        BasicInfo.logger = new ObjectLogger("./MoBoxFrp/logs");
        //周四检测（
        checkThursday();
        //外部依赖初始化（不包含MossLib）
        //FileDependency.loadDependencyDir("./MoBoxFrp/dependency", "dependency");

        //基础信息输出
        BasicInfo.logger.sendInfo("欢迎使用MoBoxFrp~这里是客户端哦~");
        BasicInfo.logger.sendInfo("软件版本：" + BasicInfo.version + " " + BasicInfo.versionType);
        BasicInfo.logger.sendInfo("软件作者：" + BasicInfo.author);
        BasicInfo.logger.sendInfo("感谢以下贡献者：");
        BasicInfo.logger.sendInfo(BasicInfo.contributor);

        //配置文件初始化
        BasicInfo.logger.sendInfo("正在读取配置文件......");
        FileCheck.checkDirExist("./MoBoxFrp/configs");
        BasicInfo.config = ConfigManager.getConfigObject("./MoBoxFrp", "config.yml", "config.yml");
        BasicInfo.debug = BasicInfo.config.getBoolean("debug");

        //检查命令行启动
        if (BasicInfo.config.getString("systemType").equals("Windows")) {
            if (System.console() == null) {
                BasicInfo.logger.sendInfo("请使用命令行启动哦！");
                TaskStartConsole.executeTask();
                System.exit(1);
            }
        }

        //文件检查
        BasicInfo.logger.sendInfo("正在检查本地文件......");
        FileCheck.checkDirExist("./MoBoxFrp/frp");
        TaskClearFile.executeTask();

        //尝试自动登录
        if (BasicInfo.config.getBoolean("autoLogin")) {
            BasicInfo.logger.sendInfo("正在尝试自动登录......");
            String account = BasicInfo.config.getString("account");
            String password = BasicInfo.config.getString("password");
            String login = BasicInfo.config.getString("login");
            TaskLogin.executeTask(login,account,password,true);
        }

        //Tick线程初始化
        BasicInfo.logger.sendInfo("正在启动Tick线程......");
        TickStatus.runTick();
        TickWatchdog.runTick();

        //WebAPI模块初始化
        BasicInfo.logger.sendInfo("正在初始化WebAPI模块......");
        WebMain.initWeb();

        //设置hook
        BasicInfo.logger.sendInfo("正在设置Shutdown Hook......");
        TaskSetHook.executeTask();

        //自动启动隧道
        BasicInfo.logger.sendInfo("正在检查自动启动隧道......");
        TaskReadConfig.executeTask(true);

        //命令行初始化
        CommandManager.initCommand(BasicInfo.logger,true);
        CommandManager.registerCommand(new CommandExit());
        CommandManager.registerCommand(new CommandDebug());
        CommandManager.registerCommand(new CommandLogin());
        CommandManager.registerCommand(new CommandConfig());
        CommandManager.registerCommand(new CommandHelp());
        CommandManager.registerCommand(new CommandReload());
        CommandManager.registerCommand(new CommandTunnel());

        //计时
        long completeTime = System.currentTimeMillis();
        BasicInfo.logger.sendInfo("======================================================================");
        BasicInfo.logger.sendInfo("启动完成！耗时："+(completeTime-startTime)+"毫秒！");
        BasicInfo.logger.sendInfo("使用指令help查询命令帮助！");
        BasicInfo.logger.sendInfo("请访问 http://127.0.0.1:"+BasicInfo.config.getInteger("httpPort")+"/ 进入管理页面！");
        BasicInfo.logger.sendInfo("======================================================================");
    }

    public static void reload() {
        BasicInfo.logger.sendInfo("正在重载配置文件......");
        BasicInfo.config = ConfigManager.getConfigObject("./MoBoxFrp", "config.yml", "config.yml");
        BasicInfo.debug = BasicInfo.config.getBoolean("debug");
        TaskReadConfig.executeTask(false);
    }

    public static void checkThursday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int index=calendar.get(Calendar.DAY_OF_WEEK)-1;
        if (index == 4) {
            try {
                throw new ThursdayKFCVMe50Exception();
            } catch (Exception e) {
                BasicInfo.logger.sendException(e);
            }
        }
    }
}
