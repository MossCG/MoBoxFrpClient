package org.moboxlab.MoBoxFrpClient.Web.Request;

import com.alibaba.fastjson.JSONObject;

public class RequestLogin {
    public static JSONObject getResult(String loginType,String account,String password) {
        String route = "/API/Login";
        JSONObject request = new JSONObject();
        request.put("loginType",loginType);
        request.put("account",account);
        request.put("password",password);
        return RequestBasic.postAPI(route,request,0);
    }
}
