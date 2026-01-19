package org.moboxlab.MoBoxFrpClient.Web.API;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Task.TaskGetUserInfo;
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
 *     "userID": "xxx",
 *     "permission": "xxx",
 *     "permissionCode": "xxx",
 *     "username": "xxx",
 *     "email": "xxx",
 *     "phone": "xxx",
 *     "qq": "xxx",
 *     "gold": "xxx",
 *     "silver": "xxx",
 *     "success": true,
 *     "message": ""
 * }
 * 错误返回：
 * {
 *     "success": false,
 *     "message": "reason"
 * }
 */

public class APIUserInfo implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        JSONObject responseData = getTemplate();
        try {
            //基础响应初始化
            WebBasic.initBasicResponse(exchange);
            String ip = WebBasic.getRemoteIP(exchange);
            BasicInfo.sendDebug(ip+" "+exchange.getRequestURI().toString());
            JSONObject requestData = WebBasic.loadRequestData(exchange);
            //登录状态判断
            if (!BasicInfo.login) {
                responseData.replace("message","没登录哦~");
                return;
            }
            //检查缓存有效期
            if (BasicInfo.timeUserInfo + 60*1000L < System.currentTimeMillis()) {
                TaskGetUserInfo.executeTask(false);
            }
            //写入响应数据
            responseData.putAll(BasicInfo.userInfo);
            responseData.replace("email","已绑定");
            responseData.replace("phone","已绑定");
            responseData.replace("qq","已绑定");
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
