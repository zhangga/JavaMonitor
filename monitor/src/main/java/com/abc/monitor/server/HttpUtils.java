package com.abc.monitor.server;

import com.abc.monitor.config.MonitorConfig;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * HTTP服务
 * Created by U-Demon
 * Date: 2019/7/4
 */
public class HttpUtils {

    /** post请求命令 */
    private static final String POST = "POST";
    /** get请求命令 */
    private static final String GET = "GET";

    /**
     * 发送监控日志
     * @param log
     * @return
     */
    public static boolean sendMonitorLog(String log) throws Exception {
        String ret = doPost(MonitorConfig.SERVER_URL + "/monitor_log", log);
        return true;
    }

    /**
     * 发送POST请求
     * @param url
     * @param content
     * @return
     */
    public static String doPost(String url, String content) throws Exception {
        HttpURLConnection httpConnection = null;
        try {
            byte[] datas = content.getBytes("UTF-8");
            URL httpUrl = new URL(url);
            httpConnection = (HttpURLConnection) httpUrl.openConnection();
            // 设定请求的方法为"POST"，默认是GET
            httpConnection.setRequestMethod(POST);
            // 设置连接主机超时（单位：毫秒）
            httpConnection.setConnectTimeout(5000);
            // 设置从主机读取数据超时（单位：毫秒）
            httpConnection.setReadTimeout(3000);
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            httpConnection.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true
            httpConnection.setDoInput(true);
            // Post 请求不能使用缓存
            httpConnection.setUseCaches(false);
            // 设置传输数据格式
            httpConnection.setRequestProperty("Content-Type", "application/msgpack");
            // 设置传输数据长度
            httpConnection.setRequestProperty("Content-Length", String.valueOf(datas.length));

            try (DataOutputStream ds = new DataOutputStream(httpConnection.getOutputStream())) {
                ds.write(datas);
                ds.flush();
            }

            return parseResult(httpConnection.getInputStream());
        } finally {
            httpConnection.disconnect();
        }
    }

    /**
     * 从输入流中解析出String
     * @param in
     * @return
     * @throws IOException
     */
    private static String parseResult(InputStream in) throws IOException {
        StringBuilder result = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        }
        return result.toString();
    }

}
