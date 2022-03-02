package com.zmj.demo.common;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Iterator;
import java.util.Set;

@Component
@Slf4j
public class HttpUtil {
    private  HttpClient client = new HttpClient();
    private  PostMethod post ;
    private  GetMethod get ;

    /**
     * post请求，json格式的数据
     * @param headers
     * @param url
     * @param params
     * @return
     */
    public  synchronized String postByJson(String url, String params,String... headers){
        post = new PostMethod(url) ;
        if (params != null) {
            RequestEntity se = null;
            try {
                se = new StringRequestEntity(params, "application/json", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            post.setRequestEntity(se);
        }

        if (headers.length != 0 ) {
            JSONObject jsonObject = JSONObject.parseObject(headers[0]);
            for (String key : jsonObject.keySet()
            ) {
                post.setRequestHeader(key, jsonObject.getString(key));
            }
        }
        try {
            int code = client.executeMethod(post);
            InputStream respone = post.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(respone));
            String tempbf;
            StringBuffer re=new StringBuffer(100);
            while((tempbf=br.readLine())!=null){
                re.append(tempbf);
            }
            return re.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            post.releaseConnection();
        }
        return null;
    }

    /**
     * post请求 text格式
     * @param headers
     * @param url
     * @param text
     * @return
     */
    public synchronized String postByText(String url,String text,String... headers){
        post = new PostMethod(url) ;
        if (text != null) {
            RequestEntity se = null;
            try {
                se = new StringRequestEntity(text, "text/plain", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            post.setRequestEntity(se);
        }

        if (headers.length != 0) {
            JSONObject jsonObject = JSONObject.parseObject(headers[0]);
            for (String key : jsonObject.keySet()
            ) {
                post.setRequestHeader(key, jsonObject.getString(key));
            }
        }
        try {
            int code = client.executeMethod(post);
            InputStream respone = post.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(respone));
            String tempbf;
            StringBuffer re=new StringBuffer(100);
            while((tempbf=br.readLine())!=null){
                re.append(tempbf);
            }
            return re.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * post请求-form方法
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public String postByForm(String url, String params,String... headers){

        post = new PostMethod(url) ;
        if (params != null) {
            if (params.contains("&")) {
                String[] temp = params.split("&");
                for (String temp1 : temp) {
                    String[] temp2 = temp1.split("=");
                    post.addParameter(temp2[0], temp2[1]);
                }
            } else {
                JSONObject p = JSONObject.parseObject(params);
                Set paramsKey = p.keySet();
                Iterator iterator = paramsKey.iterator();
                while (iterator.hasNext()) {
                    String key = String.valueOf(iterator.next());
                    String value = p.getString(key);
                    post.addParameter(key, value);
                }
            }
        }
        if (headers.length != 0){
            JSONObject jsonObject = JSONObject.parseObject(headers[0]);
            for (String key : jsonObject.keySet()
            ) {
                post.setRequestHeader(key, jsonObject.getString(key));
            }
        }
        post.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        try {
            long start = System.currentTimeMillis();
            int code = client.executeMethod(post);
            long end = System.currentTimeMillis();
            log.info("请求时间:{}ms",start-end);
            InputStream respone = post.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(respone));
            String tempbf;
            StringBuffer re=new StringBuffer(100);
            while((tempbf=br.readLine())!=null){
                re.append(tempbf);
            }
            return re.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get方法
     * @param url
     * @param headers
     * @return
     */
    public synchronized String get(String url,String... headers){
        get = new GetMethod(url) ;
        if (headers.length != 0){
            JSONObject jsonObject = JSONObject.parseObject(headers[0]);
            for (String key : jsonObject.keySet()
            ) {
                get.setRequestHeader(key, jsonObject.getString(key));
            }
        }
        try {
            int code = client.executeMethod(get);
            InputStream respone = get.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(respone));
            String tempbf;
            StringBuffer re=new StringBuffer(100);
            while((tempbf=br.readLine())!=null){
                re.append(tempbf);
            }
            return re.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
