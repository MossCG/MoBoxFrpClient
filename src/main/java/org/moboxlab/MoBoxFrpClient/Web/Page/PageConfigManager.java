package org.moboxlab.MoBoxFrpClient.Web.Page;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Web.WebBasic;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PageConfigManager implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            //基础响应初始化
            WebBasic.initBasicResponse(exchange);
            String ip = WebBasic.getRemoteIP(exchange);
            BasicInfo.sendDebug(ip+" "+exchange.getRequestURI().toString());
            //页面读取发送
            String page = WebBasic.getPageFile("./MoBoxFrp/pages/configManager.html",false);
            byte[] responseData = page.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200,responseData.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseData);
            os.close();
            //结束响应
            WebBasic.completeResponse(exchange);
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
        }
    }
}
