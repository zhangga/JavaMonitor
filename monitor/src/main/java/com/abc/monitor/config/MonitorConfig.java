package com.abc.monitor.config;

import javassist.CtMethod;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 暂不支持动态修改
 * Created by U-Demon
 * Date: 2019/7/3
 */
public class MonitorConfig {

    private static final String PATH = "./monitor.config";

    private static final String SERVER = "monitor.server=";

    public static String SERVER_URL = null;

    /** 方法监控 */
    public static Map<String, ClassMonitor> classMonitors = new HashMap<>();

    private static File configFile = null;
    private static String lastModified = null;

    /** 监控配置文件变化 */
//    private static ExecutorService service = Executors.newSingleThreadExecutor();

    public static void init() {
        configFile = new File(PATH);
        lastModified = getLastModified(configFile);
        loadConfig();
//        service.submit(LOOK_CONFIG);
    }

    private static void loadConfig() {
        classMonitors.clear();
        try (FileReader fr = new FileReader(configFile);BufferedReader br = new BufferedReader(fr)) {
            String line = null;
            while ((line = br.readLine()) != null) {
                // 注释
                if (line.trim().startsWith("#")) {
                    continue;
                }
                // 服务器配置
                if (line.startsWith(SERVER)) {
                    SERVER_URL = "http://" + line.substring(SERVER.length()).trim();
                    continue;
                }
                // 第一个#的index
                int index = line.indexOf("#");
                if (index == -1) {
                    System.err.println("monitor.config配置中没有配置#信息");
                    continue;
                }
                // 类名
                String className = line.substring(0, index);
                // 类监控
                ClassMonitor cm = classMonitors.get(className);
                if (cm == null) {
                    cm = new ClassMonitor(className);
                    classMonitors.put(className, cm);
                }
                cm.addInfo(line);
            }
            if (SERVER_URL == null) {
                throw new RuntimeException("在monitor.config配置文件中未找到 monitor.server=IP:PORT 的配置");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("在monitor.jar同目录下没有找到配置文件：monitor.config");
        }
    }

    private static Runnable LOOK_CONFIG = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String newModified = getLastModified(configFile);
                if (!lastModified.equals(newModified)) {
                    System.out.println("==================监控配置文件发生变化==================");
                    lastModified = newModified;
                    loadConfig();
                }
            }
        }
    };

    /**
     * 类是否在监控列表中
     * @param className
     * @return
     */
    public static boolean isMonitor(String className) {
        className = className.replace("/", ".");
        return classMonitors.containsKey(className);
    }

    /**
     * 获取和方法匹配的监控
     * @param ctMethod
     * @return
     */
    public static List<MethodMonitor> getMethodMonitors(CtMethod ctMethod) {
        String className = ctMethod.getDeclaringClass().getName();
        ClassMonitor cm = classMonitors.get(className);
        if (cm == null) {
            return null;
        }
        return cm.getMethodMonitors(ctMethod);
    }

    private static String getLastModified(File file) {
        if (file == null) {
            return null;
        }
        return new StringBuilder().append(file.length()).append(file.lastModified()).toString();
    }

}
