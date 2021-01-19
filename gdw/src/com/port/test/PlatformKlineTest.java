package com.port.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.port.util.BaseUtil;
import org.apache.commons.httpclient.HttpClient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PlatformKlineTest {
    public static File file1 = new File("data/platformKline1.txt");
    public static File file3 = new File("data/platformKline3.txt");
    public static File mobile = new File("/Users/gaihongwei/tools/apache-jmeter-5.1.1/bin/mobile.txt");
    public static FileWriter fw1 ;
    public static FileWriter fw3 ;
    public static FileWriter fw_mobile ;
    static {
        try {
            fw1 = new FileWriter(file1);
            fw3 = new FileWriter(file3);
            fw_mobile = new FileWriter(mobile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
//        kline_1();
//        kline_3();
//        88888100000
        for (int i = 100000 ; i <= 105000 ; i ++){
            try {
                fw_mobile.write(""+88888+i+","+i+"\r\n");
                fw_mobile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void kline_3(){
        HttpClient client = new HttpClient();
        String url = "http://192.168.112.77:10020/v1/contract/market/kline";
        //k线类型
        String param = "klineType=3&platformContractId=10&since=";
        String respose = BaseUtil.getResponse(client, url, param);
        JSONArray response_obj = JSONObject.parseObject(respose).getJSONArray("data");
        for (int i = 0 ; i < response_obj.size() ; i ++){
            try {
                fw3.write(response_obj.get(i)+"\r\n");
                fw3.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void kline_1(){
        HttpClient client = new HttpClient();
        String url = "http://192.168.112.77:10020/v1/contract/market/kline";
        //k线类型
        String param = "klineType=0&platformContractId=10&since=";
        String respose = BaseUtil.getResponse(client, url, param);
//        System.out.println(respose);
        JSONArray response_obj = JSONObject.parseObject(respose).getJSONArray("data");
//        System.out.println("size="+response_obj.size());
        List<JSONObject> kline_3 = new ArrayList<JSONObject>();
        for (int i = 0 ; i < response_obj.size() ; i += 3){
            JSONObject param_3 = new JSONObject();
            if ( i+2 < response_obj.size()) {
                param_3.put("open", response_obj.getJSONObject(i).getDouble("open"));

                Double[] temp = kline_max(response_obj.getJSONObject(i).getDouble("high"),
                        response_obj.getJSONObject(i + 1).getDouble("high"),
                        response_obj.getJSONObject(i + 2).getDouble("high"),
                        response_obj.getJSONObject(i).getDouble("low"),
                        response_obj.getJSONObject(i + 1).getDouble("low"),
                        response_obj.getJSONObject(i + 2).getDouble("low"));

                param_3.put("high", temp[0]);

                param_3.put("low", temp[1]);

                param_3.put("close", response_obj.getJSONObject(i + 2).getDouble("close"));

//                param_3.put("amount", response_obj.getJSONObject(i).getDouble("amount")
//                        + response_obj.getJSONObject(i + 1).getDouble("amount")
//                        + response_obj.getJSONObject(i + 2).getDouble("amount"));

                param_3.put("volume", response_obj.getJSONObject(i).getDouble("volume")
                        + response_obj.getJSONObject(i + 1).getDouble("volume")
                        + response_obj.getJSONObject(i + 2).getDouble("volume"));



                param_3.put("type", response_obj.getJSONObject(i).getInteger("type"));

                param_3.put("time", new BigDecimal(response_obj.getJSONObject(i+2).getDouble("time")).setScale(0, BigDecimal.ROUND_DOWN));
            }else if ( i+1 < response_obj.size()) {
                param_3.put("open", response_obj.getJSONObject(i).getDouble("open"));

                Double[] temp = kline_max(response_obj.getJSONObject(i).getDouble("high"),
                        response_obj.getJSONObject(i + 1).getDouble("high"),
                        response_obj.getJSONObject(i).getDouble("low"),
                        response_obj.getJSONObject(i + 1).getDouble("low"));

                param_3.put("high", temp[0]);

                param_3.put("low", temp[1]);

                param_3.put("close", response_obj.getJSONObject(i + 1).getDouble("close"));

                param_3.put("volume", response_obj.getJSONObject(i).getDouble("volume")
                        + response_obj.getJSONObject(i + 1).getDouble("volume"));

//                param_3.put("amount", response_obj.getJSONObject(i).getDouble("amount")
//                        + response_obj.getJSONObject(i + 1).getDouble("amount"));

                param_3.put("type", response_obj.getJSONObject(i).getDouble("type"));

                param_3.put("time", new BigDecimal(response_obj.getJSONObject(i+1).getDouble("time")).setScale(0, BigDecimal.ROUND_DOWN));
            }else{
                param_3.put("open", response_obj.getJSONObject(i).getDouble("open"));

                param_3.put("high", response_obj.getJSONObject(i).getDouble("high"));

                param_3.put("low", response_obj.getJSONObject(i).getDouble("low"));

                param_3.put("close", response_obj.getJSONObject(i ).getDouble("close"));

                param_3.put("volume", response_obj.getJSONObject(i).getDouble("volume"));

//                param_3.put("amount", response_obj.getJSONObject(i).getDouble("amount"));

                param_3.put("type", response_obj.getJSONObject(i).getDouble("type"));

                param_3.put("time", new BigDecimal(response_obj.getJSONObject(i).getDouble("time")).setScale(0, BigDecimal.ROUND_DOWN));
            }

            kline_3.add(param_3);
        }
        for (int i = 0 ; i < kline_3.size() ; i ++ ){
            try {
                fw1.write(kline_3.get(i)+"\r\n");
                fw1.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static Double[] kline_max(Double high0,Double high1,Double high2,
                                     Double low0,Double low1,Double low2){
        Double max = high0;
        Double min = low0;
        Double[] d = new Double[2];

        if (high1 > max){
            max = high1;
        }
        if (high2 > max){
            max = high2;
        }
        if (low1 < min){
            min = low1;
        }
        if (low2 < min){
            min = low2;
        }
        d[0] = max ;
        d[1] = min ;
        return d;
    }
    public static Double[] kline_max(Double high0,Double high1,
                                     Double low0,Double low1){
        Double max = high0;
        Double min = low0;
        Double[] d = new Double[2];

        if (high1 > max){
            max = high1;
        }
        if (low1 < min){
            min = low1;
        }
        d[0] = max ;
        d[1] = min ;
        return d;
    }
}
