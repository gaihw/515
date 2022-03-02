package com.zmj.demo;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.enums.MessageEnum;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Test {

    public static HttpClient client = new HttpClient();
    public static PostMethod post;
    public static GetMethod get;
    public static void main(String[] args) {
        int studentId = 380744;
//        String receiveBatch_url = "https://api.aboatedu.com/lieying-api/student/pool/receiveBatch";
//        String receiveBatch_param = "studentIdList=" + studentId;
//        String auth = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2luZm8iOnsicmVhbE5hbWUiOiLotbXnvo7pnZkiLCJwcm9qZWN0IjoiYnVpbGQseGlhb2ZhbmcsdG1hbGwseXVhbixmdWh1aSx6amdjcyxobnl6YyIsImlkIjoxMjg2MywiZW1haWwiOiJ6aGFvbWVpamluZ0BhYm9hdGVkdS5jb20iLCJvcmdJZCI6IllaQzA3NTYiLCJjcmVhdGVEYXRlIjoiMjAyMS0wNy0xOVQwOToyNDoyNS41MDUiLCJ1c2VybmFtZSI6InpoYW9tZWlqaW5nIn0sInVzZXJfbmFtZSI6InpoYW9tZWlqaW5nIiwic2NvcGUiOltdLCJleHAiOjE2MjY4MDE4NjUsImF1dGhvcml0aWVzIjpbIm1pbmdyZW50YW5nIl0sImp0aSI6ImMxYmM1ZGFkLTMwYjUtNGQzOC04M2QzLWJjYmVlZmU2NjhhNyIsImNsaWVudF9pZCI6ImxpZXlpbmcifQ.ldGJHz0ZXnYV-dI8jBXnL2iKPH_fYHpsqtsVGFcX4VKFCL1RA5EmszYS3BJaijL2-D114xiqbli-xDjY_hYnENwjARNdX59_G22OIxOCo87N24YwFnHvZH4oLzHHF-USqL3-iH_VCKelbV7HdH45g55gq67RhtGJc-ylGmxrOaOYgw_8jZ0FfJq_39Q9ic-_3UZ49C2IvDaLWryb4zbqmf1SuwAgFM4cGau-HjsZprBID7TFg2e72PvNbd5y1sCho7qA0ePKk6Ww0NTQFLZ9mlbEnT9A6meXCd9wOoFRd42a2ryJqfTb7VrPV6VEkYgjXWavAulUQMLz_VRu9XCQeA";
//        String receiveBatch_res = postByForm(receiveBatch_url, auth, receiveBatch_param);
//        System.out.println(receiveBatch_res);

//        String val = "";
//        Random random = new Random();
//        for ( int i = 0; i < "Tx9C7XoB0cXftHbaU8Tl".length(); i++ )
//        {
//            String str = random.nextInt( 2 ) % 2 == 0 ? "num" : "char";
//            if ( "char".equalsIgnoreCase( str ) )
//            { // 产生字母
//                int nextInt = random.nextInt( 2 ) % 2 == 0 ? 65 : 97;
//                // System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
//                val += (char) ( nextInt + random.nextInt( 26 ) );
//            }
//            else if ( "num".equalsIgnoreCase( str ) )
//            { // 产生数字
//                val += String.valueOf( random.nextInt( 10 ) );
//            }
//        }
//        System.out.println(val);

        System.out.println(BigDecimal.valueOf(0.067784).compareTo(BigDecimal.valueOf(0.067784)));



    }

    public static String postByForm(String url, String authorization, String params) {
        post = new PostMethod(url);
        String[] temp2;
        if (params.contains("&")) {
            temp2 = params.split("&");
            for(int  i= 0;  i< temp2.length; i++) {
                    post.addParameter(temp2[i].split("=")[0], temp2[i].split("=")[1]);
            }
        } else {
            temp2 = params.split("=");
            if (temp2.length == 1) {
                post.addParameter(temp2[0], "0");
            } else {
                post.addParameter(temp2[0], temp2[1]);
            }
        }

        post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setRequestHeader("Authorization", authorization);
        post.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        post.setRequestHeader("MenuCode", "ly-gh-ghmp");

        try {
            int code = client.executeMethod(post);
            InputStream respone = post.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(respone));
            StringBuffer re = new StringBuffer(100);

            String tempbf;
            while((tempbf = br.readLine()) != null) {
                re.append(tempbf);
            }

            return re.toString();
        } catch (IOException var9) {
            var9.printStackTrace();
            return null;
        }
    }

}
