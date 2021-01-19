
import com.alibaba.fastjson.JSONObject;
import com.port.util.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllTimeChange {
    public static void main(String[] args) {
        List<String> userIds = new ArrayList<String>();
        /* 璇诲彇鏁版嵁 */
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/userIds")),
                    "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                userIds.add(lineTxt);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
        //更新
        List<String> url_changeTime = new ArrayList<String>();
        url_changeTime.add("swaps");//币本位
        url_changeTime.add("http://192.168.200.86:8883/dynamic/config/delay/update/batch");//usdt
        url_changeTime.add("http://192.168.112.27:8884/regular/dynamic/config/delay/update/batch");//交割
        url_changeTime.add("http://192.168.112.128:8883/dynamic/config/delay/update/batch");//混合合约

        //获取
        List<String> url_getTime = new ArrayList<String>();
        url_getTime.add("http://192.168.200.168:8081/dynamic/config/delay/list");//币本位
        url_getTime.add("http://192.168.200.86:8883/dynamic/config/delay/list");//usdt
        url_getTime.add("http://192.168.112.27:8884/regular/dynamic/config/delay/list");//交割
        url_getTime.add("http://192.168.112.128:8883/dynamic/config/delay/list");//混合合约

//        getTime(userIds,url_getTime.get(1));
        changeTime(userIds,url_changeTime.get(3),3);
        System.out.println("用户="+userIds.size());
    }

    /**
     * 获取延时
     * @param userIds
     */
    public static void getTime(List<String> userIds,String url){
        for(String userId:userIds){
            String params = "{\"userId\":\""+userId+"\"}";
            System.out.println(BaseUtil.postByForm(url,params));
        }
    }

    /**
     * 批量更新延时
     * @param userIds
     */
    public static void changeTime(List<String> userIds,String url,int index)  {
        FileWriter fw = null;
        try {
            fw = new FileWriter(new File("data/params"));
            List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
            List time = new ArrayList();
            for (int i = 0; i < 9; i++) {
                int a = (int)(Math.random()*30+1);
                int random = (a >= 20 ) ? a * 100 : a * 1000;
                time.add(random);
            }
            int maxCtranct = 9 ;
            for(String userId:userIds){
                JSONObject jsonb = new JSONObject();
                jsonb.put("userId",userId);
                //币本位
                if (index == 0){
                    if(userId.equals("333388222")){
                        jsonb.put("contractDelayMap",JSONObject.parseObject("{\"4\":\"2000\",\"5\":\"2000\",\"6\":\"1200\",\"7\":\"1200\",\"8\":\"7000\",\"9\":\"7500\"}"));
                    }else if (userId.equals("333388223")){
                        jsonb.put("contractDelayMap",JSONObject.parseObject("{\"1\":\"1200\",\"2\":\"7000\",\"3\":\"7500\",\"4\":\"1200\",\"5\":\"2000\",\"6\":\"2200\",\"7\":\"7000\",\"8\":\"7200\",\"9\":\"2100\"}"));
                    }else if(userId.equals("333385143")){
                        jsonb.put("contractDelayMap",JSONObject.parseObject("{\"1\":\"2100\",\"2\":\"8500\",\"3\":\"6000\",\"4\":\"1200\",\"5\":\"6000\",\"6\":\"2100\"}"));
                    }else {
                        jsonb.put("contractDelayMap", JSONObject.parseObject("{\"1\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"2\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"3\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\"," +
                                "\"4\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"5\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"6\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\"," +
                                "\"7\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"8\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"9\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\"}"));
                    }
                    //usdt
                }else if (index == 1){
                    if(userId.equals("333388222")){
                        jsonb.put("contractDelayMap",JSONObject.parseObject("{\"1004\":\"2000\",\"1005\":\"2000\",\"1006\":\"1200\",\"1007\":\"1200\",\"1008\":\"7000\",\"1009\":\"7500\"}"));
                    }else if (userId.equals("333388223")){
                        jsonb.put("contractDelayMap",JSONObject.parseObject("{\"1001\":\"1200\",\"1002\":\"7000\",\"1003\":\"7500\",\"1004\":\"1200\",\"1005\":\"2000\",\"1006\":\"2200\",\"1007\":\"7000\",\"1008\":\"7200\",\"1009\":\"2100\"}"));
                    }else if(userId.equals("333385099")){
                        jsonb.put("contractDelayMap",JSONObject.parseObject("{\"1001\":\"2100\",\"1002\":\"8500\",\"1003\":\"6000\",\"1004\":\"1200\",\"1005\":\"6000\",\"1006\":\"2100\"}"));
                    }else {
                        jsonb.put("contractDelayMap", JSONObject.parseObject("{\"1001\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"1002\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"1003\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\"," +
                                "\"1004\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"1005\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"1006\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\"," +
                                "\"1007\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"1008\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"1009\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\"}"));
                    }
                    //交割
                }else if (index == 2){
                    if(userId.equals("333388222")){
                        jsonb.put("contractDelayMap",JSONObject.parseObject("{\"2004\":\"2000\",\"2005\":\"2000\",\"2006\":\"1200\",\"2008\":\"7000\",\"2009\":\"7500\"}"));
                    }else if (userId.equals("333388223")){
                        jsonb.put("contractDelayMap",JSONObject.parseObject("{\"2001\":\"1200\",\"2002\":\"7000\",\"2003\":\"7500\",\"2004\":\"1200\",\"2005\":\"2000\",\"2006\":\"2200\",\"2008\":\"7200\",\"2009\":\"2100\"}"));
                    }else if(userId.equals("333385099")){
                        jsonb.put("contractDelayMap",JSONObject.parseObject("{\"2001\":\"2100\",\"2002\":\"8500\",\"2003\":\"6000\",\"2004\":\"1200\",\"2005\":\"6000\",\"2006\":\"2100\",\"2008\":\"1200\",\"2009\":\"7000\"}"));
                    }else {
                        jsonb.put("contractDelayMap", JSONObject.parseObject("{\"2001\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"2002\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"2003\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\"," +
                                "\"2004\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"2005\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"2006\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\"," +
                                "\"2007\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"2008\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"2009\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\"}"));
                    }
                    //混合
                }else{
                    if(userId.equals("333388222")){
                        jsonb.put("contractDelayMap",JSONObject.parseObject("{\"4004\":\"2000\",\"4005\":\"2000\",\"4006\":\"1200\",\"4007\":\"1200\",\"4008\":\"7000\",\"4009\":\"7500\"}"));
                    }else if (userId.equals("333388223")){
                        jsonb.put("contractDelayMap",JSONObject.parseObject("{\"4001\":\"1200\",\"4002\":\"7000\",\"4003\":\"7500\",\"4004\":\"1200\",\"4005\":\"2000\",\"4006\":\"2200\",\"4007\":\"7000\",\"4008\":\"7200\",\"4009\":\"2100\"}"));
                    }else if(userId.equals("333385099")){
                        jsonb.put("contractDelayMap",JSONObject.parseObject("{\"4001\":\"2100\",\"4002\":\"8500\",\"4003\":\"6000\",\"4004\":\"1200\",\"4005\":\"6000\",\"4006\":\"2100\"}"));
                    }else {
                        jsonb.put("contractDelayMap", JSONObject.parseObject("{\"4001\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"4002\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"4003\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\"," +
                                "\"4004\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"4005\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"4006\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\"," +
                                "\"4007\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"4008\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\",\"4009\":\"" + time.get((int) (Math.random() * maxCtranct)) + "\"}"));
                    }
                }

                list.add(jsonb);
            }
            String params = list.toString();
            System.out.println(BaseUtil.postByJson(url,params));
            fw.write(params);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
