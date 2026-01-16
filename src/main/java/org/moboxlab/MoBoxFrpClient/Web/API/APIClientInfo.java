package org.moboxlab.MoBoxFrpClient.Web.API;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
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
 *     "version": "xxx", 版本号
 *     "versionType": "xxx", 版本类型，Beta为测试版，Stable为稳定版
 *     "author": "xxx", 作者
 *     "contributor": "xxx", 贡献者
 *     "memoryUsage": "xxx", 内存使用量（GB）
 *     "memoryMax": "xxx", 内存总量（GB）
 *     "cpuUsage": "xxx", CPU使用率（百分比，取值为0.0-100.0）
 *     "bandUpload": "xxx", 上传带宽（Mbps）
 *     "bandDownload": "xxx", 下载带宽（Mbps）
 *     "bandUploadTotal": "xxx", 上传流量（GB）
 *     "bandDownloadTotal": "xxx", 下载流量（GB）
 *     "success": true,
 *     "message": ""
 * }
 * 错误返回：
 * {
 *     "success": false,
 *     "message": "reason"
 * }
 */

public class APIClientInfo implements HttpHandler {
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
        json.put("version",BasicInfo.version);
        json.put("versionType",BasicInfo.versionType);
        json.put("author",BasicInfo.author);
        json.put("contributor",BasicInfo.contributor);
        json.put("memoryUsage",BasicInfo.memoryUsage);
        json.put("memoryMax",BasicInfo.memoryMax);
        json.put("cpuUsage",BasicInfo.cpuUsage);
        json.put("bandUpload",BasicInfo.bandUpload);
        json.put("bandDownload",BasicInfo.bandDownload);
        json.put("bandUploadTotal",BasicInfo.bandUploadTotal);
        json.put("bandDownloadTotal",BasicInfo.bandDownloadTotal);
        json.put("success",false);
        json.put("message","");
        return json;
    }
}
