package com.zmj.demo.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@Slf4j
public class HttpUtils {
    public  HttpClient client = new HttpClient();
    public  PostMethod post ;
    public  GetMethod get ;

    /**
     * 发送json格式的数据
     * @param url
     * @param params
     * @return
     */
    public  String postByJson(String url, String params){
        post = new PostMethod(url) ;
        RequestEntity se = null;
        try {
            se = new StringRequestEntity(params,"application/json" ,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setRequestEntity(se);
        post.setRequestHeader("Content-Type","application/json");
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
     * 发送带token的json数据
     * @param url
     * @param authorization
     * @param params
     * @return
     */
    public  String postByJson(String url,String authorization, String params){
        post = new PostMethod(url) ;
        RequestEntity se = null;
        try {
            se = new StringRequestEntity(params,"application/json" ,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setRequestEntity(se);
        post.setRequestHeader("Content-Type","application/json");
        post.setRequestHeader("Authorization","Basic "+authorization);
        try {
//            System.out.println("开始："+df.format(new Date()));// new Date()为获取当前系统时间
            int code = client.executeMethod(post);
//            System.out.println("结束："+df.format(new Date()));// new Date()为获取当前系统时间
            InputStream respone = post.getResponseBodyAsStream();
//            System.out.println("结束："+df.format(new Date()));// new Date()为获取当前系统时间
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
     * 发送json格式的数据
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
     * @param url
     * @return
     */
    public  String postByText(String url,String text){
        post = new PostMethod(url) ;
        RequestEntity se = null;
        try {
            se = new StringRequestEntity(text,"text/plain" ,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setRequestEntity(se);
        post.setRequestHeader("Content-Type","text/plain");
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

    /**
     *
     * @param url
     * @param creb
     * @param text
     * @return
     */
    public  String postByText(String url,String creb,String text){
        post = new PostMethod(url) ;
        RequestEntity se = null;
        try {
            se = new StringRequestEntity(text,"text/plain" ,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setRequestEntity(se);
        post.setRequestHeader("Content-Type","text/plain");
        post.setRequestHeader("Authorization","Basic "+creb);
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
}
