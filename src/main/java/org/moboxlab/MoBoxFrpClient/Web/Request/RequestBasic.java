package org.moboxlab.MoBoxFrpClient.Web.Request;

import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Task.TaskLogin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RequestBasic {
    @SuppressWarnings("ExtractMethodRecommender")
    public static JSONObject postAPI(String route, JSONObject data, boolean retry, int depth) {
        try {
            //建立连接
            URL targetURL = new URL(BasicInfo.api+route);
            HttpURLConnection connection = (HttpURLConnection) targetURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Type","application/json;charset=utf8");
            connection.setRequestProperty("User-Agent", "MoBoxFrpClient/"+BasicInfo.version+BasicInfo.versionType);
            connection.setDoOutput(true);
            //写入请求数据
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
            writer.write(data.toString());
            writer.flush();
            writer.close();
            //读取响应数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder readInfo = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) readInfo.append(inputLine);
            reader.close();
            //返回数据
            JSONObject result = JSONObject.parseObject(readInfo.toString());
            BasicInfo.sendDebug(result.toJSONString());
            //如果提示未登录，重新登录
            if (!result.getBoolean("success") && BasicInfo.login && depth<3 && retry) {
                BasicInfo.logger.sendWarn("API请求失败，正在尝试重新登录！");
                TaskLogin.executeTask(BasicInfo.loginType,BasicInfo.account,BasicInfo.password,false);
                data.replace("token", BasicInfo.token);
                return postAPI(route, data, true, depth+1);
            }
            return result;
        } catch (Exception e) {
            //出错
            BasicInfo.logger.sendException(e);
            BasicInfo.sendDebug(data.toJSONString());
            BasicInfo.logger.sendWarn("API请求失败，请查看debug信息以确认问题！");
            return null;
        }
    }
}
