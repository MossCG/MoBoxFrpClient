package org.moboxlab.MoBoxFrpClient.Task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Web.API.APIAdGet;
import org.moboxlab.MoBoxFrpClient.Web.Request.RequestAD;
import org.moboxlab.MoBoxFrpClient.Web.Request.RequestUserInfo;

public class TaskGetAd {
    public static void executeTask() {
        JSONObject adData = RequestAD.getResult();
        JSONObject adInfo = adData.getJSONObject("data");;
        adInfo.replace("url_jump","https://www.moboxfrp.top"+adInfo.get("url_jump"));
        BasicInfo.adInfo = adInfo;
        BasicInfo.timeAdInfo = System.currentTimeMillis();
    }
}
