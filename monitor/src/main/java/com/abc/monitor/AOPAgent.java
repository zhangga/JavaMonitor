package com.abc.monitor;

import com.abc.monitor.config.MonitorConfig;
import com.abc.monitor.server.MonitorServer;

import java.lang.instrument.Instrumentation;

/**
 * Created by U-Demon
 * Date: 2019/7/3
 */
public class AOPAgent {

    private static Instrumentation _inst = null;

    /**
     * premain方法
     * @param path
     * @param inst
     */
    public static void premain(String path, Instrumentation inst) {
        System.out.println("启动性能监控...");
        // 读取配置
        MonitorConfig.init();
        // 启动服务
        MonitorServer.start();

        _inst = inst;
        _inst.addTransformer(new AOPAgentTransformer());
    }

}
