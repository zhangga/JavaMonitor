package com.abc.monitor.impl;

import javassist.CannotCompileException;
import javassist.CtMethod;

/**
 * Created by U-Demon
 * Date: 2019/7/4
 */
public class MonitorInsertAfter extends BaseMonitor {
    @Override
    public void doMonitor(CtMethod ctMethod) throws CannotCompileException {
        String after = "{System.out.println($_);" +
                "System.out.println(" + "\"" + ctMethod.getDeclaringClass().getSimpleName() + "." + ctMethod.getName()
                + "() cost: " + "\"" + " + (System.currentTimeMillis() - 1)" + " + \" ms\");}";
        System.out.println("【MonitorInsertAfter】 after: " + after);
        ctMethod.insertAfter(after);
    }
}
