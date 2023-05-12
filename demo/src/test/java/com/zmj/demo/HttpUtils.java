package com.zmj.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class HttpUtils {
    private HttpClient client = new HttpClient();
    private PostMethod post;

    /**
     * post请求，json格式的数据
     *
     * @param headers
     * @param url
     * @param params
     * @return
     */
    public synchronized String postByJson(String url, String params, String headers) {

        boolean printlog = true;
        if (printlog) {
            System.out.println(">>>>>>");
            System.out.println(">>> url: ");
            System.out.println(url);
            System.out.println("========== request ==========");
            System.out.println(">>> params: ");
            System.out.println(params);
            System.out.println(">>> headers: ");
            System.out.println(headers);
        }
        //
        post = new PostMethod(url);
        if (params != null) {
            RequestEntity se = null;
            try {
                se = new StringRequestEntity(params, "application/json", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            post.setRequestEntity(se);
        }

        try {
            // header
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> hm = mapper.readValue(headers, Map.class);
            hm.forEach(
                    (k,v) -> {
                        post.setRequestHeader(k, v);
                    }
            );
            //
            client.executeMethod(post);
            InputStream respone = post.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(respone));
            String tempbf;
            StringBuffer re = new StringBuffer(100);
            while ((tempbf = br.readLine()) != null) {
                re.append(tempbf);
            }
            //
            if (printlog) {
                System.out.println("========== response ==========");
                System.out.println(re);
                System.out.println(">>>>>>");
            }
            //
            return re.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
        return null;
    }
}
