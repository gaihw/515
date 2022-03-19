package allin;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class BaseUtils {
    public static HttpClient client = new HttpClient();
    public static PostMethod post ;
    public static GetMethod get ;
    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    static {
        System.setProperty("fileName", "baseutils/baseutils.log");
    }
    public static Logger log = LoggerFactory.getLogger(BaseUtils.class);
    /**
     * 发送表单
     * @param url
     * @param params
     * @return
     */
    public static String postByForm(String url, String params){
        post = new PostMethod(url) ;
        if (params.contains("&")){
            String[] temp = params.split("&");
            for (String temp1:temp){
                String[] temp2 = temp1.split("=");
                post.addParameter(temp2[0],temp2[1]);
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
        post.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        try {
            System.out.println("开始："+df.format(new Date()));// new Date()为获取当前系统时间
            int code = client.executeMethod(post);
            System.out.println("结束："+df.format(new Date()));// new Date()为获取当前系统时间
            InputStream respone = post.getResponseBodyAsStream();
            System.out.println("结束："+df.format(new Date()));// new Date()为获取当前系统时间
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
     * 发送带登录token的表单数据
     * @param url
     * @param authorization
     * @param params
     * @return
     */
    public static String postByForm(String url, String authorization,String params){
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
        post.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        post.setRequestHeader("ACCESS_TOKEN",authorization);
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
        post.setRequestHeader("Authorization",authorization);
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
     * 发送带token的json数据
     * @param url
     * @param token
     * @param uid
     * @param params
     * @return
     */
    public static String postByJson(String url,String token,String uid, String params){
        post = new PostMethod(url) ;
        RequestEntity se = null;
        try {
            se = new StringRequestEntity(params,"application/json" ,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setRequestEntity(se);
        post.setRequestHeader("Content-Type","application/json");
        post.setRequestHeader("token",token);
        post.setRequestHeader("uid",uid);
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
     * 发送表单数据
     * @param url
     * @return
     */
    public static String getByForm(String url){
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

    public static void writerData1(String fileName,String text) throws IOException {
        File file = new File("data/"+fileName);
        if (!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(text+",\r\n");
        bw.flush();
        bw.close();
        fw.close();
    }
}
