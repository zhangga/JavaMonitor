package com.abc.monitor.impl;

import javassist.CannotCompileException;
import javassist.CtMethod;

/**
 * Created by U-Demon
 * Date: 2019/7/4
 */
public class MonitorInsertBefore extends BaseMonitor {
    /**
     * 使用"{}"将多语句括起来
     * @param ctMethod
     * @throws CannotCompileException
     */
    @Override
    public void doMonitor(CtMethod ctMethod) throws CannotCompileException {
        String before = "{long monitor_start_time = System.currentTimeMillis(); System.out.println(monitor_start_time); return monitor_start_time;}";
//        before = new MonitorLog("slsjdksadljsakdsa").getJavaCode();
        System.out.println("【MonitorInsertBefore】 before: " + before);
        ctMethod.insertBefore(before);
    }
}
