package newly;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Test {
    static {
        System.setProperty("fileName", "test/test.log");
    }
    public static String shop_id = "";
    public static Logger log = LoggerFactory.getLogger(Test.class);
    public static void main(String[] args) throws IOException {
//        calc();
        HashMap<String,Integer> hashMap = new HashMap();
        hashMap.put("a",1);
        hashMap.put("b",2);

        Set<String> set = hashMap.keySet();
        Iterator iterator = set.iterator();

        while (iterator.hasNext()){
            String key = (String)iterator.next();
            System.out.println(key+":"+hashMap.get(key));
        }
        for (String ke : set){
            System.out.println(ke);
        }
        System.out.println("=====1====");

        for (Map.Entry<String,Integer> e : hashMap.entrySet()){
            System.out.println(e.getKey()+"::"+e.getValue());
        }

        System.out.println("=====2====");

        Iterator<Map.Entry<String,Integer>> it = hashMap.entrySet().iterator();
        while (it.hasNext()){
            System.out.println(it.next().getKey()+":::"+it.next().getValue());

        }
        System.out.println("=====3====");
    }

    private static void calc() {
        String res = BaseUtils.getByForm("http://goic.sftcwl.com:8091/resource/organization/getaoiinfo?id=1000000006",
                "logisticsCenter=6; STOKEN=AAADIcAABbICsMQ05pW1xxWRASREdmZqTRCui9_gVeI8kQh1T6tCzNMAEAcAAKj8Fxb8SlsVPfb4EwqJAADMYakZGbWCHRW3Exz6twAAPvSvH8XFAADxFwAAV-GXLGUC; CSTOKEN=AAADIcAABbICsMQ05pW1xxWRASREdmZqTRCui9_gVeI8kQh1T6tCzNMAEAcAAKj8Fxb8SlsVPfb4EwqJAADMYakZGbWCHRW3Exz6twAAPvSvH8XFAADxFwAAV-GXLGUC; Hm_lvt_b34be861e3f8fe6bf76ea90a4ec4fb93=1617162126,1617162131,1617246408,1617246630; Hm_lpvt_b34be861e3f8fe6bf76ea90a4ec4fb93=1617246630");
        //店铺信息
        JSONObject jsonObject = JSONObject.parseObject(res).getJSONObject("data").getJSONObject("shop_list");

//        System.out.println(getAllShop(jsonObject));
        //定义list,存储店铺和对应的坐标
        List shop_xy = new ArrayList();
        for (String s: shop_id.split(",")
             ) {
            List temp = new ArrayList();
            temp.add(s);
            temp.add(jsonObject.getJSONObject(s).getJSONArray("shop_position"));
            shop_xy.add(temp);
        }
        System.out.println(shop_xy);

        //两个店铺最近距离
        BigDecimal min = BigDecimal.valueOf(0.0);
        //两个店铺最远距离
        BigDecimal max = BigDecimal.valueOf(0.0);
        //所有店铺间的距离和
        BigDecimal sum = BigDecimal.valueOf(0.0);
        //两个店铺最远距离是哪两个
        Map shop = new HashMap<>();


        //遍历店铺，计算俩两距离
        for (int i = 0; i < shop_xy.size()-1; i++) {
            List l1 = (List) shop_xy.get(i);
            for (int j = i+1; j < shop_xy.size(); j++) {
                List l2 = (List) shop_xy.get(j);
                BigDecimal dis = distance((List) l1.get(1),(List)l2.get(1));
                System.out.println("one="+l1.get(0)+"::two="+l2.get(0)+"::"+dis);
                sum = sum.add(dis);
                //如果当前距离大于最大距离，则更新
                if(dis.compareTo(max)==1){
                    shop.put("one",l1.get(0));
                    shop.put("two",l2.get(0));
                    max = dis;
                }
                //如果当前距离小于最小距离，则更新
                if (dis.compareTo(min)==-1){
                    min = dis;
                }
            }
        }
        System.out.println("sum="+sum.divide(BigDecimal.valueOf(shop_xy.size()),3, RoundingMode.DOWN));
        System.out.println("max="+max.multiply(BigDecimal.valueOf(100)));
        System.out.println("min="+min);
        System.out.println(shop);
    }

    /**
     * 获取所有店铺
     * @param jsonObject
     * @return
     */
    public static String getAllShop(JSONObject jsonObject){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for (String s:jsonObject.keySet()
             ) {
            stringBuilder.append(s+",");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    /**
     * 计算俩点的距离
     * @param arr1
     * @param arr2
     * @return
     */
    public static BigDecimal distance(List arr1,List arr2){
        BigDecimal x1 = BigDecimal.valueOf((Double.valueOf( arr1.get(0).toString())));
        BigDecimal y1 = BigDecimal.valueOf((Double.valueOf(arr1.get(1).toString())) );

        BigDecimal x2 = BigDecimal.valueOf((Double.valueOf(arr2.get(0).toString())) );
        BigDecimal y2 = BigDecimal.valueOf((Double.valueOf(arr2.get(1).toString())) );

        BigDecimal qs =  ((x1.subtract(x2).abs())
                .multiply(x1.subtract(x2).abs()))
                .add(
                        (y1.subtract(y2).abs())
                                .multiply(y1.subtract(y2).abs())
                );
        return BigDecimal.valueOf(Math.sqrt(qs.doubleValue()));
    };
}
