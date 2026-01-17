package org.moboxlab.MoBoxFrpClient.Object;

import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Task.TaskRemoveConfig;
import org.moboxlab.MoBoxFrpClient.Task.TaskTunnelStop;
import org.moboxlab.MoBoxFrpClient.Task.TaskWriteConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ObjectProcess {
    //隧道名称
    public String name;
    //隧道基本数据
    public JSONObject data;

    //守护线程
    public Thread daemon;
    //进程本体
    public Process process;
    //日志数据
    public StringBuffer logBuffer;

    //配置文件名称
    public String configFile;
    //可执行文件名称
    public String executeFile;

    //启动方法
    public void start() throws Exception{
        BasicInfo.logger.sendInfo("正在启动隧道："+name);
        //写入配置
        configFile = "./MoBoxFrp/frp/"+name+".toml";
        executeFile = "./MoBoxFrp/frp/frpc-"+name;
        if (BasicInfo.config.getString("systemType").equals("Windows")) executeFile += ".exe";
        TaskWriteConfig.executeTask(name,data);
        logBuffer = new StringBuffer();
        //启动进程
        String command = executeFile+" -c "+configFile;
        process = Runtime.getRuntime().exec(command);
        daemon = new Thread(() -> daemonVoid(this));
        daemon.start();
    }

    //停止方法
    @SuppressWarnings("deprecation")
    public void stop() throws Exception{
        BasicInfo.logger.sendInfo("正在停止隧道："+name);
        //停止进程
        //我知道stop弃用了但是interrupt不好使
        daemon.stop();
        process.destroy();
        //删除配置
        TaskRemoveConfig.executeTask(name);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void daemonVoid(ObjectProcess object) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(object.process.getInputStream(), StandardCharsets.UTF_8));
        while (true) {
            try {
                String readLine;
                while ((readLine = reader.readLine())!=null) {
                    object.logBuffer.append(readLine).append("\r\n");
                    BasicInfo.sendDebug(readLine);
                    //日志翻译及处理
                    String prefix = "["+object.name+"]";
                    if (readLine.contains("try to connect to server...")) {
                        BasicInfo.logger.sendInfo(prefix+"正在连接至服务器......");
                    }
                    if (readLine.contains("login to server success")) {
                        BasicInfo.logger.sendInfo(prefix+"登录成功！");
                    }
                    if (readLine.contains("start proxy success")) {
                        BasicInfo.logger.sendInfo(prefix+"隧道启动成功！");
                    }
                    if (readLine.contains("connect to server error")) {
                        if (readLine.contains("no such host")) {
                            BasicInfo.logger.sendWarn(prefix+"隧道启动失败：域名解析失败");
                        }
                        if (readLine.contains("target machine actively refused it")) {
                            BasicInfo.logger.sendWarn(prefix+"隧道启动失败：目标服务器拒绝链接");
                        }
                        BasicInfo.logger.sendWarn(prefix+"隧道启动异常！已自动关闭隧道！");
                        object.asyncStop();
                    }
                    if (readLine.contains("start error")) {
                        if (readLine.contains("port already used")) {
                            BasicInfo.logger.sendWarn(prefix+"隧道启动失败：端口已被占用");
                        }
                        if (readLine.contains("already exists")) {
                            BasicInfo.logger.sendWarn(prefix+"隧道启动失败：同名隧道已存在");
                        }
                        BasicInfo.logger.sendWarn(prefix+"隧道启动异常！已自动关闭隧道！");
                        object.asyncStop();
                    }
                }
            } catch (Exception e) {
                BasicInfo.logger.sendException(e);
                BasicInfo.logger.sendWarn("守护进程出现错误！隧道名称："+object.name);
            }
        }
    }

    public void asyncStop() {
        Thread thread = new Thread(() -> TaskTunnelStop.executeTask(name));
        thread.start();
    }
}
