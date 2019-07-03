package com.abc.monitor.impl;

import com.abc.monitor.server.MonitorServer;
import javassist.CannotCompileException;
import javassist.CtMethod;

/**
 * 监控方法整体执行时间
 * Created by U-Demon
 * Date: 2019/7/3
 */
public class MonitorTimeOnly extends BaseMonitor {
    @Override
    public void doMonitor(CtMethod ctMethod) throws CannotCompileException {
        String before = "long monitor_start_time = System.currentTimeMillis(); System.out.println(monitor_start_time);";
        String after = "System.out.println(" + "\"" + ctMethod.getDeclaringClass().getSimpleName() + "." + ctMethod.getName()
                + "() cost: " + "\"" + " + (System.currentTimeMillis() /*- monitor_start_time*/)" + " + \" ms\");";
        System.out.println("【MonitorTimeOnly】 before: " + before);
        System.out.println("【MonitorTimeOnly】 after: " + after);
        ctMethod.insertBefore(before);
        ctMethod.insertAfter(after);
    }
}
