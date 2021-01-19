package com.mutest.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author guozhengMu
 * @version 1.0
 * @date 2018/12/20 11:47
 * @description http请求的基本方法
 * @modify
 */
public class URLConnection {

    public static String httpGet(String path, Map<String, String> headers) {
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("connection", "keep-Alive");
            // Get请求不需要DoOutPut
            conn.setDoOutput(false);
            conn.setDoInput(true);
            // 设置连接超时时间和读取超时时间
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // 遍历设置信息头
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
            // 连接服务器
            conn.connect();
            // 取得输入流，并使用Reader读取
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("异常：" + e.getMessage());
            return e.getMessage();
        }
        // 关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    public static String httpPost(String path, String postData, Map<String, String> headers) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置连接超时时间和读取超时时间
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            // 遍历设置信息头
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            out = new OutputStreamWriter(conn.getOutputStream());
            // POST的请求参数写在正文中
            out.write(postData);
            out.flush();
            out.close();
            // 取得输入流，并使用Reader读取
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        // 关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    public static String httpPost(String path, JSONObject postData, Map<String, String> headers) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置连接超时时间和读取超时时间
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            // conn.setRequestProperty(headerKey, headerValue); // 设置信息头
            conn.setRequestProperty("Content-Type", "application/json");
            // 遍历设置信息头
            if (!headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            out = new OutputStreamWriter(conn.getOutputStream());
            // POST的请求参数写在正文中
            out.write(postData.toString());
            out.flush();
            out.close();
            // 取得输入流，并使用Reader读取
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            // e.printStackTrace();
        }
        // 关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }
}
