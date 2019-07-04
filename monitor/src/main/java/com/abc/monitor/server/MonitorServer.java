package com.abc.monitor.server;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * 监控服务
 * Created by U-Demon
 * Date: 2019/7/3
 */
public class MonitorServer {

    /** 日志队列 */
    private static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
    /** 失败日志队列 */
    private static ConcurrentLinkedDeque<String> failedQueue = new ConcurrentLinkedDeque<>();

    /** 发送间隔时间：毫秒 */
    private static final int interval = 1000;
    /** 单次发送上限 */
    private static final int max = 500;
    /** 失败日志最多缓存多少条 */
    private static final int failedMax = 100000;

    /** 日志间分隔符 */
    private static final String SEPARATION = "#mlog#";

    private static Random random = new Random();

    /**
     * 提交监控日志
     * @param log
     */
    public static void submit(String log) {
        queue.add(log);
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
//        Timer timer1 = new Timer();
//        timer1.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 100; i++) {
//                    submit("测试日志。。。");
//                }
//            }
//        }, 1000, 100);
    }

    /**
     * 发送日志
     */
    private static void sendMonitorLog() {
        StringBuilder logs = new StringBuilder();
        int count = sendFailedLog(logs);
        // 如果失败日志队列有堆积，优先处理
        if (count > 0) {
            // 当前队列日志直接加入失败队列
            moveToFailed();
        }
        // 正常发送日志队列
        else {
            String log = null;
            // 调节发送速率
            int sendMax = queue.size() > max * 4 ? max * 2 : max;
            // 打包日志一起发送
            while (count < sendMax && (log = queue.poll()) != null) {
                logs.append(log).append(SEPARATION);
                count++;
            }
            // 如果超过发送速率的10倍，直接加入失败队列
            if (queue.size() > max * 10) {
                System.err.println("监控日志堆积过多！请注意持续观察！！！当前数量：" + queue.size());
                moveToFailed();
            }
        }
        // 发送
        if (count > 0) {
            String logStr = logs.toString();
            try {
                HttpUtils.sendMonitorLog(logStr);
            }
            catch (Exception e) {
                System.err.println("监控服务方出现故障！请检查！！！");
                rollbackLogs(logStr);
            }
            // 几率输出日志
            if (random.nextInt(100) < 5) {
                System.out.println("监控系统正在运行中...");
            }
        }
    }

    /**
     * 失败日志
     * @param logs
     * @return
     */
    private static int sendFailedLog(StringBuilder logs) {
        if (failedQueue.isEmpty()) {
            return 0;
        }
        int count = 0;
        String log = null;
        while (count < max * 3 && (log = failedQueue.poll()) != null) {
            logs.append(log).append(SEPARATION);
            count++;
        }
        return count;
    }

    private static void moveToFailed() {
        // 当前队列日志直接加入失败队列
        while (!queue.isEmpty()) {
            failedQueue.add(queue.poll());
        }
    }

    /**
     * 回写未成功的日志
     * @param logs
     */
    private static void rollbackLogs(String logs) {
        if (logs == null || logs.length() == 0) {
            return;
        }
        // 超出失败上限，丢弃
        if (failedQueue.size() > failedMax) {
            System.err.println("失败日志堆积已达上限，丢弃！");
            while (failedQueue.size() > failedMax) {
                failedQueue.poll();
            }
            return;
        }
        // 回写。
        String[] logArr = logs.split(SEPARATION);
        for (String log : logArr) {
            if (failedQueue.size() < failedMax) {
                failedQueue.addFirst(log);
            }
            else {
                System.err.println("失败日志堆积已达上限，丢弃！");
                break;
            }
        }
    }

}
