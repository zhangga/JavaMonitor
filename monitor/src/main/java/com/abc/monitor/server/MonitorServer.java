package com.abc.monitor.server;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * 监控服务
 * Created by U-Demon
 * Date: 2019/7/3
 */
public class MonitorServer {

    private static ConcurrentLinkedQueue<MonitorLog> queue = new ConcurrentLinkedQueue<>();

    /** 发送间隔时间：毫秒 */
    private static final int interval = 2000;
    /** 单次发送上限 */
    private static final int max = 250;

    /**
     * 提交监控日志
     * @param log
     */
    public static void submit(String log) {
        queue.add(new MonitorLog(log));
    }

    /**
     * 启动监控服务
     */
    public static void start() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendMonitorLog();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000, interval);

        // 测试
        Timer timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                submit("测试日志。。。");
            }
        }, 1000, 100);
    }

    /**
     * 发送日志
     */
    private static void sendMonitorLog() {
        int count = 0;
        MonitorLog log = null;
        while (count < max && (log = queue.poll()) != null) {
            //TODO
            System.out.println("处理日志： " + log.getLog());
            count++;
        }
    }

}
