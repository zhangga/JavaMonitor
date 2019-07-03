package com.abc.monitor.impl;

import javassist.CannotCompileException;
import javassist.CtMethod;

/**
 * 监控的
 * Created by U-Demon
 * Date: 2019/7/3
 */
public abstract class BaseMonitor {

    public BaseMonitor() {

    }

    /** 监控的具体逻辑 */
    public abstract void doMonitor(CtMethod ctMethod) throws CannotCompileException;

}
