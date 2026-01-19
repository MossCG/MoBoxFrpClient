package org.moboxlab.MoBoxFrpClient.Web.Request;

import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrpClient.BasicInfo;

public class RequestCodes {
    public static JSONObject getResult() {
        String route = "/API/UserCode/List";
        JSONObject request = new JSONObject();
        request.put("token", BasicInfo.token);
        return RequestBasic.postAPI(route,request,true,0);
    }
}
