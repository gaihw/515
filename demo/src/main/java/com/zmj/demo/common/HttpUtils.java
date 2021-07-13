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
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class HttpUtils {
    public  HttpClient client = new HttpClient();
    public  PostMethod post ;
    public  GetMethod get ;

    /**
     * post请求，json格式的数据
     * @param headerMap
     * @param url
     * @param params
     * @return
     */
    public  String postByJson(Map<String,String> headerMap, String url, String params){
        post = new PostMethod(url) ;
        RequestEntity se = null;
        try {
            se = new StringRequestEntity(params,"application/json" ,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setRequestEntity(se);

        for (Map.Entry<String,String> entry:headerMap.entrySet()) {
            post.setRequestHeader(entry.getKey(),entry.getValue());
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
     * post请求，无参数
     * @param url
     * @return
     */
    public  String postByJson(String url){
        post = new PostMethod(url) ;
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
     * post请求 text格式
     * @param headerMap
     * @param url
     * @param text
     * @return
     */
    public  String postByText(Map<String,String> headerMap,String url,String text){
        post = new PostMethod(url) ;
        RequestEntity se = null;
        try {
            se = new StringRequestEntity(text,"text/plain" ,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setRequestEntity(se);
//        post.setRequestHeader("Content-Type","text/plain");
        for (Map.Entry<String,String> entry:headerMap.entrySet()) {
            post.setRequestHeader(entry.getKey(),entry.getValue());
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
     * post请求 form格式的参数
     * @param headerMap
     * @param url
     * @param params
     * @return
     */
    public String postByForm(Map<String,String> headerMap,String url,String params){
        post = new PostMethod(url) ;
        if (params.contains("&")){
            String[] temp = params.split("&");
            for (String temp1:temp){
                String[] temp2 = temp1.split("=");
                if (temp2.length==1){
                    post.addParameter(temp2[0],"0");
                }else{
                    post.addParameter(temp2[0],temp2[1]);
                }
            }
        }else{
            JSONObject p = JSONObject.parseObject(params);
            Set paramsKey = p.keySet();
            Iterator iterator = paramsKey.iterator();
            while (iterator.hasNext()){
                String key = String.valueOf(iterator.next());
                String value = p.getString(key);
                post.addParameter(key,value);
            }
        }
//        post.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        for (Map.Entry<String,String> entry:headerMap.entrySet()) {
            post.setRequestHeader(entry.getKey(),entry.getValue());
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
     * get方法
     * @param url
     * @return
     */
    public String get(String url){
        get = new GetMethod(url) ;
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
