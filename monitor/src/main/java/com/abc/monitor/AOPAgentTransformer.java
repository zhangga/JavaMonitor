package com.abc.monitor;

import com.abc.monitor.config.MethodMonitor;
import com.abc.monitor.config.MonitorConfig;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

/**
 * Created by U-Demon
 * Date: 2019/7/3
 */
public class AOPAgentTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!MonitorConfig.isMonitor(className)) {
            return classfileBuffer;
        }

        byte[] transformed = null;
        ClassPool pool = null;
        CtClass cl = null;
        try {
            pool = ClassPool.getDefault();
            cl = pool.makeClass(new ByteArrayInputStream(classfileBuffer));

            CodeConverter converter = new CodeConverter();

            if (!cl.isInterface()) {
                CtMethod[] methods = cl.getDeclaredMethods();
                for (int i = 0; i < methods.length; i++) {
                    CtMethod ctMethod = methods[i];
                    List<MethodMonitor> mms = MonitorConfig.getMethodMonitors(ctMethod);
                    if (mms == null || mms.isEmpty()) {
                        continue;
                    }
                    // 监控方法
                    mms.forEach((mm) -> {
                        mm.monitor(ctMethod);
                    });
                }
                transformed = cl.toBytecode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } finally {
            if (cl != null) {
                cl.detach();
            }
        }
        return transformed;
    }

    private void AOPInsertMethod(CtMethod method) throws NotFoundException, CannotCompileException {
        // 添加监控耗时
        method.instrument(new ExprEditor(){
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                m.replace("{ long stime = System.currentTimeMillis(); $_ = $proceed($$);System.out.println(\""
                        + m.getClassName() + "." + m.getMethodName()
                        + " cost:\" + (System.currentTimeMillis() - stime) + \" ms\");}");
            }
        });
        // 在方法体前后插入语句
        method.insertBefore("System.out.println(\"Insert into method before\");");
        method.insertAfter("System.out.println(\"Insert into method after\");");
    }

}
