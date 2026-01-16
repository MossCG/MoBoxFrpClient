package org.moboxlab.MoBoxFrpClient.Web.API;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Task.TaskLogin;
import org.moboxlab.MoBoxFrpClient.Web.WebBasic;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * API结构：
 * 请求：
 * {
 *     "loginType": "phone/email",
 *     "account": "xxx",
 *     "password": "xxx"
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

public class APILogin implements HttpHandler {
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
            String loginType = requestData.getString("loginType");
            String account = requestData.getString("account");
            String password = requestData.getString("password");
            //执行登录
            TaskLogin.executeTask(loginType,account,password);
            //写入响应数据
            responseData.replace("success",BasicInfo.login);
            if (!BasicInfo.login) {
                responseData.replace("message","登录失败！请检查账号密码或网络链接！");
            }
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
