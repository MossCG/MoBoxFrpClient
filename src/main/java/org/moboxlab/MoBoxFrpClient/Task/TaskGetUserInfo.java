package org.moboxlab.MoBoxFrpClient.Task;

import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Web.Request.RequestUserInfo;

public class TaskGetUserInfo {
    public static void executeTask(boolean display) {
        JSONObject userInfo = RequestUserInfo.getResult();
        if (userInfo != null && display) {
            BasicInfo.logger.sendInfo("用户ID："+userInfo.getString("userID"));
            BasicInfo.logger.sendInfo("用户名称："+userInfo.getString("username"));
            BasicInfo.logger.sendInfo("用户权限："+userInfo.getString("permission"));
            BasicInfo.logger.sendInfo("绑定邮箱："+userInfo.containsKey("email"));
            BasicInfo.logger.sendInfo("绑定手机："+userInfo.containsKey("phone"));
            BasicInfo.logger.sendInfo("绑定QQ账号："+userInfo.containsKey("qq"));
            BasicInfo.userInfo = userInfo;
        }
    }
}
