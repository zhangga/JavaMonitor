package com.abc.monitor_server.config;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by U-Demon
 * Date: 2019/7/4
 */
public class RET {

    private static final String RET = "ret";
    private static final String REASON = "reason";

    public static final String OK;

    static {
        JSONObject JOK = new JSONObject();
        JOK.put(RET, 1);
        OK = JOK.toJSONString();
    }

    public static JSONObject getJson(int ret, String reason) {
        JSONObject result = new JSONObject();
        result.put(RET, ret);
        result.put(REASON, reason);
        return result;
    }

    public static String get(int ret, String reason) {
        JSONObject json = getJson(ret, reason);
        return json.toJSONString();
    }

}
