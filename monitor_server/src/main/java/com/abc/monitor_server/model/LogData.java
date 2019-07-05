package com.abc.monitor_server.model;

import java.util.Date;

/**
 * Created by U-Demon
 * Date: 2019/7/4
 */
public class LogData {

    private Long id;

    private String class_name;

    private String method_name;

    private Long run_time;

    private Date log_time;

    public LogData() {

    }

    public static LogData getData(String log) {
        String[] datas = log.split("#");
        if (datas.length < 4)
            return null;
        LogData data = new LogData();
        data.class_name = datas[0];
        data.method_name = datas[1];
        data.run_time = Long.valueOf(datas[2]);
        data.log_time = new Date(Long.valueOf(datas[3]));
        return data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getMethod_name() {
        return method_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    public Long getRun_time() {
        return run_time;
    }

    public void setRun_time(Long run_time) {
        this.run_time = run_time;
    }

    public Date getLog_time() {
        return log_time;
    }

    public void setLog_time(Date log_time) {
        this.log_time = log_time;
    }
}
