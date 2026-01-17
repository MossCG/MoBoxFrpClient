package org.moboxlab.MoBoxFrpClient.Task;

import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.mossmc.mosscg.MossLib.File.FileCheck;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TaskWriteConfig {
    public static void executeTask(String name, JSONObject data){
        try {
            //文件路径
            String configFile = "./MoBoxFrp/frp/"+name+".toml";
            //服务器配置
            StringBuilder builder = new StringBuilder();
            builder.append("serverAddr = \"").append(data.getString("serverAddress")).append("\"\r\n");
            builder.append("serverPort = ").append(data.getString("serverPort")).append("\r\n");
            builder.append("auth.token = \"").append(data.getString("token")).append("\"\r\n");

            //空行
            builder.append("\r\n");

            //代理配置
            builder.append("[[proxies]]").append("\r\n");
            builder.append("name = \"").append(data.getString("name")).append("\"\r\n");
            builder.append("type = \"").append(data.getString("protocol")).append("\"\r\n");
            builder.append("localIP = \"").append(data.getString("localAddress")).append("\"\r\n");
            builder.append("localPort = ").append(data.getString("localPort")).append("\r\n");
            builder.append("remotePort = ").append(data.getString("openPort")).append("\r\n");

            //写入配置文件
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(configFile)), StandardCharsets.UTF_8));
            writer.write(builder.toString());
            writer.flush();
            writer.close();

            //释放frp本体
            String systemType = BasicInfo.config.getString("systemType");
            switch (systemType) {
                case "Windows":
                    FileCheck.checkFileExist("./MoBoxFrp/frp/frpc-"+name+".exe","frp/frpc.exe");
                    break;
                case "Linux":
                    FileCheck.checkFileExist("./MoBoxFrp/frp/frpc-"+name,"frp/frpc");
                    break;
                default:
                    BasicInfo.logger.sendWarn("不支持的平台类型: " + systemType);
                    break;
            }
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendWarn("执行任务WriteConfig时出现异常！");
        }
    }
}
