package com.abc.monitor.impl;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by U-Demon
 * Date: 2019/7/3
 */
public enum EnumMonitorType {

    TIME_ONLY(1, MonitorTimeOnly.class, "监控方法的执行时间"),
    TIME_RECURSIVE(2, MonitorTimeRecursive.class, "监控方法中所有方法的执行时间"),
    ;

    /** 类型 */
    private int type;

    /** 监控 */
    private BaseMonitor monitor;

    private String desc;

    EnumMonitorType(int type, Class<? extends BaseMonitor> clazz, String desc) {
        this.type = type;
        this.desc = desc;
        try {
            this.monitor = clazz.getConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public BaseMonitor getMonitor() {
        return monitor;
    }

}
