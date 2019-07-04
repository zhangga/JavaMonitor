package com.abc.monitor.server;

/**
 * 监控日志
 * Created by U-Demon
 * Date: 2019/7/3
 */
public class MonitorLog {

    private static final String CLASS_NAME = MonitorServer.class.getName();

    private static final String METHOD_SUBMIT = ".submit";

    private static final String SPLIT = "#";

    private String log;

    public MonitorLog(String log) {
        this.log = "\"" + log + "\"";
    }

    public MonitorLog(String className, String methodName, String start, String end, String logTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(className).append(SPLIT).append(methodName).append(SPLIT).append("\"+")
                .append("(").append(end).append("-").append(start).append(")+\"").append(SPLIT).append("\"+").append(logTime);
        this.log = sb.toString();
    }

    /**
     * 提交日志的java代码
     * @return
     */
    public String getJavaCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                // 对应JAVA代码：MonitorServer.submit(log);
                .append(CLASS_NAME).append(METHOD_SUBMIT).append("(")
                .append(log)
                .append(");")
                .append("}");
        String code = sb.toString();
//        System.out.println("getJavaCode: " + code);
        return code;
    }

    public String getLog() {
        return log;
    }

}
