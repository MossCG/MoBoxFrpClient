package org.moboxlab.MoBoxFrpClient.Web.API;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Task.TaskDeleteConfig;
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

public class APIDeleteConfig implements HttpHandler {
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
            //操作删除
            TaskDeleteConfig.executeTask(id);
            //写入响应数据
            responseData.replace("success",true);
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
