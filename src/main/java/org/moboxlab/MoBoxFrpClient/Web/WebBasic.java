package org.moboxlab.MoBoxFrpClient.Web;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.moboxlab.MoBoxFrpClient.BasicInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class WebBasic {
    public static String getRemoteIP(HttpExchange exchange) {
        return exchange.getRemoteAddress().getAddress().getHostAddress();
    }

    public static JSONObject loadRequestData(HttpExchange request) throws Exception{
        InputStream inputStream = request.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder text = new StringBuilder();
        String read;
        while ((read = reader.readLine())!=null) text.append(read);
        return JSONObject.parseObject(text.toString());
    }

    public static void initBasicResponse(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "*");
        headers.add("Access-Control-Max-Age", "864000");
        headers.add("Access-Control-Allow-Headers", "*");
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.set("Content-Type", "text/html; charset=UTF-8");
    }

    public static void completeResponse(HttpExchange exchange){
        try {
            exchange.close();
        } catch (Exception e) {
            BasicInfo.logger.sendWarn("关闭Web链接时发生错误，原因："+e.getMessage());
        }
    }

    public static Map<String,String> pageCache = new HashMap<>();

    public static String getPageFile(String path,boolean skipCache) {
        try {
            if (pageCache.containsKey(path) && !skipCache) return pageCache.get(path);
            String pageStr = new String(Files.readAllBytes(Paths.get(path)),StandardCharsets.UTF_8);
            pageCache.put(path,pageStr);
            BasicInfo.sendDebug(path+" updated!");
            return pageStr;
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            return "404";
        }
    }
}
