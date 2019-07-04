package com.abc.monitor_server.controller;

import com.abc.monitor_server.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 监控服务
 * Created by U-Demon
 * Date: 2019/7/4
 */
@RestController
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    /**
     * 提交监控日志请求
     * @param logs
     * @return
     */
    @PostMapping("/monitor_log")
    public String submitLog(@RequestBody String logs) {
        return monitorService.addMonitorLog(logs);
    }

}
