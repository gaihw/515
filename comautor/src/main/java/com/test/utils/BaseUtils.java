package com.test.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.databene.feed4testng.FeedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.*;

public class BaseUtils extends FeedTest {
    static {
        System.setProperty("fileName", "baseUtils/log.txt");
    }
    public static Logger log = LoggerFactory.getLogger(BaseUtils.class);
    public static HttpClient client = new HttpClient();
    public static PostMethod post ;
    public static GetMethod get;

    /**
     * get方法
     * @param url
     * @return
     */
    public static String get(String url){
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
     * 发送json格式的数据
     * @param url
     * @param params
     * @return
     */
    public static String postByJson(String url, String params){
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
    public static String postByJson(String url,String authorization, String params){
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
    public static String postByJson(String url){
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
    public static String postByText(String url,String text){
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
     * 文本参数
     * @param url
     * @param creb
     * @param text
     * @return
     */
    public static String postByText(String url,String creb,String text){
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


    @BeforeClass
    public void setUp(){

    }
   @AfterClass
    public void tearDown(){

    }
    public static long time() {
    	return System.currentTimeMillis();
    }

}