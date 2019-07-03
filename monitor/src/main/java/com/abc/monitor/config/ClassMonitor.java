package com.abc.monitor.config;

import javassist.CtMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * 类监控
 * Created by U-Demon
 * Date: 2019/7/3
 */
public class ClassMonitor {

    /** 类名 */
    private String className;

    /** 方法监控 */
    List<MethodMonitor> methodMonitors = new ArrayList<>();

    public ClassMonitor(String name) {
        this.className = name;
    }

    /**
     * 添加监控信息
     * @param info
     */
    public void addInfo(String info) {
        MethodMonitor mm = new MethodMonitor();
        mm.init(info);
        methodMonitors.add(mm);
    }

    /**
     * 获取和方法匹配的监控列表
     * @param ctMethod
     * @return
     */
    public List<MethodMonitor> getMethodMonitors(CtMethod ctMethod) {
        List<MethodMonitor> list = new ArrayList<>();
        methodMonitors.forEach((mm) -> {
            if (mm.isMatch(ctMethod)) {
                list.add(mm);
            }
        });
        return list;
    }

}
