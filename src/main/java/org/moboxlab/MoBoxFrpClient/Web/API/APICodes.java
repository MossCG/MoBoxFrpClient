package org.moboxlab.MoBoxFrpClient.Web.API;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Task.TaskGetCodes;
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
 *     "codes": [{...},{...}],
 *     "success": true,
 *     "message": ""
 * }
 * codes为JSONArray
 * 内部数据结构参考如下
 * {
 *     "codeID": "101", 穿透码ID（自增唯一ID）
 *     "status": "running",  穿透码状态（outdated，running，banned）
 *     "token": "3sz126040682333323333",  节点穿透码（建议可复制+部分打码显示）
 *     "node": "sz1",  节点名称
 *     "number": "1145141",  穿透码编号（随机七位编号）
 *     "band": "10",  穿透码带宽（Mbps）
 *     "portServer": "50000",   服务端口
 *     "portOpen": "60000-60009,61000",    开放端口
 *     "timeCreate": "1637661815000",  创建时间（毫秒时间戳）
 *     "timeOutdate": "1966173815000"    到期时间（毫秒时间戳）
 * }
 * 错误返回：
 * {
 *     "codes": [],
 *     "success": false,
 *     "message": "reason"
 * }
 */

public class APICodes implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        JSONObject responseData = getTemplate();
        try {
            //基础响应初始化
            WebBasic.initBasicResponse(exchange);
            String ip = WebBasic.getRemoteIP(exchange);
            BasicInfo.sendDebug(ip+" "+exchange.getRequestURI().toString());
            JSONObject requestData = WebBasic.loadRequestData(exchange);
            //检查缓存有效期
            if (BasicInfo.timeCodeInfo + 45*1000L < System.currentTimeMillis()) {
                TaskGetCodes.executeTask(false);
            }
            //写入响应数据
            responseData.replace("codes", BasicInfo.codeInfo);
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
        json.put("codes",new JSONArray());
        json.put("success",false);
        json.put("message","");
        return json;
    }
}
