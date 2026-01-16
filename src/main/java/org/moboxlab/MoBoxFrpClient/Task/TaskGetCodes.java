package org.moboxlab.MoBoxFrpClient.Task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Web.Request.RequestCodes;
import org.moboxlab.MoBoxFrpClient.Web.Request.RequestUserInfo;

public class TaskGetCodes {
    public static void executeTask(boolean display) {
        JSONObject codeInfo = RequestCodes.getResult();
        JSONArray codes = codeInfo.getJSONArray("codes");
        JSONArray newCodes = new JSONArray();
        if (display) {
            BasicInfo.logger.sendInfo("穿透码数据更新成功！共获取到："+codes.size()+"个可用穿透码！");
            codes.forEach(o->{
                JSONObject code = (JSONObject) o;
                if (code.getString("status").equals("running")) {
                    String codeMessage = "";
                    codeMessage += ("#"+code.getString("codeID"));
                    codeMessage += (" | "+code.getString("node"));
                    codeMessage += ("-"+code.getString("number"));
                    codeMessage += ("-"+code.getString("band")+"Mbps");
                    BasicInfo.logger.sendInfo(codeMessage);
                    newCodes.add(code);
                }
            });
        }
        BasicInfo.codeInfo = newCodes;
    }
}
