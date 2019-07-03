package com.abc.monitor.impl;

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
                m.replace("{ long stime = System.currentTimeMillis(); $_ = $proceed($$);System.out.println(\""
                        + m.getClassName() + "." + m.getMethodName()
                        + " cost:\" + (System.currentTimeMillis() - stime) + \" ms\");}");
            }
        });
    }
}
