package com.abc.monitor.impl;

import com.abc.monitor.server.MonitorLog;
import javassist.CannotCompileException;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

/**
 * 监控方法中所有方法的执行时间
 * Created by U-Demon
 * Date: 2019/7/3
 */
public class MonitorTimeRecursive extends BaseMonitor {
    @Override
    public void doMonitor(CtMethod ctMethod) throws CannotCompileException {
        // 添加监控耗时
        ctMethod.instrument(new ExprEditor(){
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                String className = m.getClassName();
                String methodName = m.getMethodName();
                if (className.startsWith("java.")) {
                    return;
                }
                m.replace("{ long _stime = System.nanoTime(); $_ = $proceed($$); "
                        + "long _etime = System.nanoTime(); long _ctime = System.currentTimeMillis(); "
                        + new MonitorLog(className, methodName, "_stime", "_etime", "_ctime").getJavaCode()
                        + "}");
            }
        });
    }
}
