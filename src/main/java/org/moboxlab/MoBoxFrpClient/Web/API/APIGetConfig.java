package org.moboxlab.MoBoxFrpClient.Web.API;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Cache.CacheConfig;
import org.moboxlab.MoBoxFrpClient.Web.WebBasic;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * API结构：
 * 请求：
 * {
 * }
 * 正确返回：
 * {
 *     "configs": {
 *         "cfg1": {
 *             ...
 *         },
 *         "cfg2": {
 *             ...
 *         }
 *     }
 *     "success": true,
 *     "message": ""
 * }
 * 其中configs为配置文件列表，其中的配置文件参考如下：
 * （key是ID，例如cfg1）
 * "cfg1": {
 *     "name": "example",
 *     "token": "3sz122123775697402749",
 *     "serverAddress": "sz1.moboxfrp.cn",
 *     "serverPort": "55446",
 *     "openPort": "55447",
 *     "localAddress": "127.0.0.1",
 *     "localPort": "25565",
 *     "protocol": "tcp",
 *     "compress": false,
 *     "autoStart": true
 * }
 * 错误返回：
 * {
 *     "configs": {},
 *     "success": false,
 *     "message": "reason"
 * }
 */

public class APIGetConfig implements HttpHandler {
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
            JSONObject data = new JSONObject();
            data.putAll(CacheConfig.configCache);
            //写入响应数据
            responseData.replace("configs",data);
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
        json.put("configs",new JSONObject());
        json.put("success",false);
        json.put("message","");
        return json;
    }
}
