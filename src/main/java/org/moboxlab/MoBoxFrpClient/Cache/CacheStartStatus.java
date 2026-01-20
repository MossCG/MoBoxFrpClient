package org.moboxlab.MoBoxFrpClient.Cache;

import java.util.HashMap;
import java.util.Map;

public class CacheStartStatus {
    private static Map<String,String> statusMap = new HashMap<>();

    public static void setStatus(String ID,String status) {
        statusMap.put(ID,status);
    }

    public static void clearStatus(String ID) {
        statusMap.remove(ID);
    }

    public static String getStatus(String ID) {
        if (statusMap.containsKey(ID)) {
            String result = statusMap.get(ID);
            statusMap.remove(ID);
            return result;
        }
        return null;
    }
}
