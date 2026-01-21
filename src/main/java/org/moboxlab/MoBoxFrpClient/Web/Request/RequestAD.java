package org.moboxlab.MoBoxFrpClient.Web.Request;

import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrpClient.BasicInfo;

public class RequestAD {
    public static JSONObject getResult() {
        String route = "/API/Ad/Get";
        JSONObject request = new JSONObject();
        request.put("token", BasicInfo.token);
        return RequestBasic.postAPI(route,request,false,0);
    }
}
