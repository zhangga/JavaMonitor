package com.abc.monitor_server.service;

import com.abc.monitor_server.config.RET;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by U-Demon
 * Date: 2019/7/4
 */
@Service
public class MonitorService {

    /** 日志队列 */
    private ConcurrentLinkedQueue<String> logQueue = new ConcurrentLinkedQueue<>();

    /** 每秒数据库写入操作 */
    private static final int OPS = 1500;

    /**
     * 添加监控日志
     * @param logs
     */
    public String addMonitorLog(String logs) {
        String[] logArr = logs.split("#mlog#");
        for (String log : logArr) {
            if (log.length() == 0)
                continue;
            // 队列暂时无上限
            logQueue.add(log);
        }
        return RET.OK;
    }

    /**
     * 处理监控日志
     */
    @Scheduled(initialDelay = 1000, fixedRate = 1000)
    @Async
    public void handleMonitorLogs() {
        int count = 0;
        String log = null;
        while (count < OPS && (log = logQueue.poll()) != null) {
            count++;
        }
        // 高负载
        if (count > OPS * 0.8) {
            System.err.println("当前系统OPS高达：" + count + "，注意持续观测，防范风险发送。");
        }
    }

}
