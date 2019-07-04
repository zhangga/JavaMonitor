package com.abc.monitor.server;

/**
 * 监控日志
 * Created by U-Demon
 * Date: 2019/7/3
 */
public class MonitorLog {

    private static final String CLASS_NAME = MonitorServer.class.getName();

    private static final String METHOD_SUBMIT = ".submit";

    private String log;

    public MonitorLog(String log) {
        this.log = log;
    }

    /**
     * 提交日志的java代码
     * @return
     */
    public String getJavaCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                // MonitorServer.submit(log);
                .append(CLASS_NAME).append(METHOD_SUBMIT).append("(\"")
                .append(log)
                .append("\");")
                .append("}");
        return sb.toString();
    }

    public String getLog() {
        return log;
    }

}
