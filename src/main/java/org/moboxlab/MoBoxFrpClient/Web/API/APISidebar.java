package org.moboxlab.MoBoxFrpClient.Web.API;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Cache.CacheSidebar;
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
 *     "sidebars": [{...},{...}],
 *     "success": true,
 *     "message": ""
 * }
 * sidebars为JSONArray
 * 内部数据结构参考如下
 * [
 *   {
 *     "id": "main-home",
 *     "name": "主页",
 *     "icon": "\uD83C\uDFE0",
 *     "type": "single",
 *     "order": 1,
 *     "url": "/info"
 *   },
 *   {
 *     "id": "main-services",
 *     "name": "服务",
 *     "icon": "\uD83D\uDD27",
 *     "type": "group",
 *     "order": 2,
 *     "submenus": [
 *       {
 *         "id": "services-status",
 *         "name": "节点状态",
 *         "url": "/services/status",
 *         "order": 1
 *       },
 *       {
 *         "id": "services-premium",
 *         "name": "独享节点",
 *         "url": "/services/premium",
 *         "order": 2
 *       },
 *       {
 *         "id": "services-codes",
 *         "name": "穿透码列表",
 *         "url": "/services/codes",
 *         "order": 3
 *       }
 *     ]
 *   }
 * ]
 * 错误返回：
 * {
 *     "sidebars": [],
 *     "success": false,
 *     "message": "reason"
 * }
 */

public class APISidebar implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        JSONObject responseData = getTemplate();
        try {
            //基础响应初始化
            WebBasic.initBasicResponse(exchange);
            String ip = WebBasic.getRemoteIP(exchange);
            BasicInfo.sendDebug(ip+" "+exchange.getRequestURI().toString());
            JSONObject requestData = WebBasic.loadRequestData(exchange);
            //写入响应数据
            responseData.replace("sidebars", CacheSidebar.sidebarCache);
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
        json.put("sidebars",new JSONArray());
        json.put("success",false);
        json.put("message","");
        return json;
    }
}
