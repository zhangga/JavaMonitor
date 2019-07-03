package com.abc.monitor.config;

import com.abc.monitor.impl.EnumMonitorType;
import javassist.CtMethod;

/**
 * 方法监控
 * Created by U-Demon
 * Date: 2019/7/3
 */
public class MethodMonitor {

    /** 方法名 */
    private String name;

    /** 参数描述 */
    private String desc;
    private String[] descArr;

    /** 监控类型 */
    private EnumMonitorType monitorType = EnumMonitorType.TIME_ONLY;

    /**
     * 初始化配置
     * @param info
     */
    public void init(String info) {
        String[] infos = info.split("#");
        int leftIndex = infos[1].indexOf("(");
        int rightIndex = infos[1].indexOf(")");
        // 名称
        this.name = infos[1].substring(0, leftIndex);
        // 描述符
        this.desc = infos[1].substring(leftIndex + 1, rightIndex);
        String[] ds = this.desc.split(",");
        this.descArr = new String[ds.length];
        for (int i = 0; i < ds.length; i++) {
            descArr[i] = ds[i].trim();
        }
        // 监控类型
        if (infos.length > 2) {
            this.monitorType = EnumMonitorType.valueOf(infos[2]);
        }
    }

    /**
     * 是否和监控方法匹配
     * @param ctMethod
     * @return
     */
    public boolean isMatch(CtMethod ctMethod) {
        String name = ctMethod.getName();
        // 匹配方法名
        if (!name.equals(this.name)) {
            return false;
        }

        String longName = ctMethod.getLongName();
        String paramDesc = longName.substring(longName.indexOf("(") +  1, longName.indexOf(")"));
        // 都无参数
        if (this.desc.trim().length() == 0 && paramDesc.trim().length() == 0) {
            return true;
        }

        String[] paramTypes = paramDesc.split(",");
        // 参数长度匹配
        if (paramTypes.length != this.descArr.length) {
            return false;
        }

        // 依次匹配参数
        for (int i = 0; i < paramTypes.length; i++) {
            int lastIndex = paramTypes[i].trim().lastIndexOf(".");
            String paramType = paramTypes[i].substring(lastIndex + 1);
            // 通配符
            if (paramType.equals("*")) {
                continue;
            }
            // 匹配类型
            if (!paramType.toLowerCase().equals(this.descArr[i].toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 监控方法
     * @param ctMethod
     */
    public void monitor(CtMethod ctMethod) {
        try {
            monitorType.getMonitor().doMonitor(ctMethod);
        } catch (Exception e) {
            System.err.println("监控方法出现异常：");
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

}
