package org.moboxlab.MoBoxFrpClient.Web.API;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Cache.CacheProcess;
import org.moboxlab.MoBoxFrpClient.Cache.CacheStartStatus;
import org.moboxlab.MoBoxFrpClient.Task.TaskTunnelStart;
import org.moboxlab.MoBoxFrpClient.Web.WebBasic;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * API结构：
 * 请求：
 * {
 *     "ID": "cfg1"
 * }
 * 正确返回：
 * {
 *     "success": true,
 *     "message": ""
 * }
 * 错误返回：
 * {
 *     "success": false,
 *     "message": "reason"
 * }
 */

public class APIStartTunnel implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        JSONObject responseData = getTemplate();
        try {
            //基础响应初始化
            WebBasic.initBasicResponse(exchange);
            String ip = WebBasic.getRemoteIP(exchange);
            BasicInfo.sendDebug(ip+" "+exchange.getRequestURI().toString());
            JSONObject requestData = WebBasic.loadRequestData(exchange);
            //获取数据
            String id = requestData.getString("ID");
            //检查是否存在
            if (CacheProcess.processMap.containsKey(id)) {
                responseData.replace("message","这个隧道已经在运行了哦！");
                return;
            }
            //执行启动
            TaskTunnelStart.executeTask(id);
            //尝试获取结果
            String result = "获取启动结果失败！请检查命令行信息！";
            for (int i = 0; i < 90; i++) {
                Thread.sleep(100L);
                String status = CacheStartStatus.getStatus(id);
                if (status == null) continue;
                if (status.contains("启动成功")) responseData.replace("success",true);
                result = status;
                break;
            }
            //写入响应数据
            responseData.replace("message",result);
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            responseData.replace("message","操作失败！服务异常！");
        } finally {
            //返回响应
            byte[] response = responseData.toString().getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200,response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
            WebBasic.completeResponse(exchange);
        }
    }

    public JSONObject getTemplate() {
        JSONObject json = new JSONObject();
        json.put("success",false);
        json.put("message","");
        return json;
    }
}
