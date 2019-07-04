package com.abc.monitor.impl;

import com.abc.monitor.server.MonitorLog;
import javassist.CannotCompileException;
import javassist.CtMethod;
import javassist.CtNewMethod;

/**
 * 监控方法整体执行时间
 * Created by U-Demon
 * Date: 2019/7/3
 */
public class MonitorTimeOnly extends BaseMonitor {
    private final static String start_time = "_monitor_start_time";
    private final static String end_time = "_monitor_end_time";
    private final static String curr_time = "_monitor_curr_time";
    private final static String prefix = "\nlong " + start_time + " = System.nanoTime();\n";
    private final static String postfix = "\nlong " + end_time + " = System.nanoTime();\n";
    private final static String curr = "\nlong " + curr_time + " = System.currentTimeMillis();\n";
    @Override
    public void doMonitor(CtMethod ctMethod) throws CannotCompileException {
        String className = ctMethod.getDeclaringClass().getName();
        String methodName = ctMethod.getName();
        // 新定义一个方法叫做比如xxx$old
        String newMethodName = methodName + "$old";
        // 将原来的方法名字修改
        ctMethod.setName(newMethodName);

        // 创建新的方法，复制原来的方法，名字为原来的名字
        CtMethod newMethod = CtNewMethod.copy(ctMethod, methodName, ctMethod.getDeclaringClass(), null);

        // 构建新的方法
        StringBuilder body = new StringBuilder();
        body.append("{")
            .append(prefix)
            .append(newMethodName + "($$);\n")// 调用原有代码，类似于method();($$)表示所有的参数
            .append(postfix)
            .append(curr)
            .append(new MonitorLog(className, methodName, start_time, end_time, curr_time).getJavaCode())
            .append("}");

        newMethod.setBody(body.toString());// 替换新方法
        ctMethod.getDeclaringClass().addMethod(newMethod);// 增加新方法
    }
}
