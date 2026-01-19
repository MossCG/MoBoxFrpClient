package org.moboxlab.MoBoxFrpClient.Task;

import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Web.Request.RequestLogin;

public class TaskLogin {
    public static void executeTask(String loginType,String account,String password,boolean display) {
        JSONObject loginResult = RequestLogin.getResult(loginType, account, password);
        if (loginResult == null) {
            BasicInfo.logger.sendWarn("登录失败！");
            return;
        }
        if (!loginResult.getBoolean("success")) {
            BasicInfo.logger.sendWarn("登录失败！");
            BasicInfo.logger.sendWarn("失败原因："+loginResult.getString("message"));
            BasicInfo.login = false;
        } else {
            BasicInfo.logger.sendInfo("登录成功！");
            BasicInfo.token = loginResult.getString("token");
            BasicInfo.login = true;
            BasicInfo.loginType = loginType;
            BasicInfo.account = account;
            BasicInfo.password = password;
            TaskGetUserInfo.executeTask(display);
            TaskGetCodes.executeTask(display);
        }
    }
}
