package old.Calc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UsdtKline {
    static {
        System.setProperty("fileName", "usdtKilne/usdtKilne.log");
    }
    public static Logger log = LoggerFactory.getLogger(UsdtKline.class);
    public static void main(String[] args) {
        String contranctId = "1035";
        String type = "0";//0-1分钟；1-3分钟；2-5分钟；3-15分钟；4-30分钟；5-1小时；6-2小时；7-4小时；8-6小时；9-12小时；10-天；11-周；12-月；13-年
        String since = "2020-12-06 16:00:00";
        String end = "2020-12-06 16:44:00";
        String startTime = dateToStamp(since);
        String endTime = dateToStamp(end);

        kline(contranctId,type,startTime,endTime);


//        System.out.println("startTime="+startTime);
//        System.out.println("endTime="+endTime);
//        String url = "https://hqesonline.9188zhibo.com/future_usdt_real_time_deals/_search";
//        String params_24 = "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"contract_id\":\""+contranctId+"\"}},{\"range\":{\"created_date\":{\"gte\":\""+startTime+"000\",\"lt\":\""+endTime+"000\"}}}],\"must_not\":[],\"should\":[]}},\"from\":0,\"size\":1,\"sort\":[],\"aggs\":{\"sum_size\":{\"sum\":{\"field\":\"size\"}},\"min_price\":{\"min\":{\"field\":\"price\"}},\"max_price\":{\"max\":{\"field\":\"price\"}}}}";
//        String res_24 = BaseUtils.postByJson(url,params_24);
//        System.out.println("最高价："+ JSONObject.parseObject(res_24).getJSONObject("aggregations").getJSONObject("max_price").getString("value"));
//        System.out.println("最低价："+ JSONObject.parseObject(res_24).getJSONObject("aggregations").getJSONObject("min_price").getString("value"));
//        System.out.println("24h成交量："+ JSONObject.parseObject(res_24).getJSONObject("aggregations").getJSONObject("sum_size").getString("value"));



    }
    public static void kline(String contranctId,String type,String since,String end){
        String url_58 = "https://reusdtapi.chuanmaolicai.com/usdt/market/bar?product="+contranctId+"&type="+type+"&since="+since+"&end="+end;
        JSONArray jsonArray = JSONObject.parseObject(BaseUtils.getByForm(url_58)).getJSONObject("data").getJSONArray("bar_data");
        BigDecimal tmp_open ;
        BigDecimal tmp_high;
        BigDecimal tmp_low;
        BigDecimal tmp_close;
        BigDecimal startTime;
        BigDecimal endTime;
        BigDecimal type_time = BigDecimal.valueOf(5*60).multiply(BigDecimal.valueOf(1000));

        String url = "https://hqesonline.9188zhibo.com/future_usdt_real_time_deals/_search";
        String res_kline_high_low;
        String res_kline_close;
        String res_kline_open;

        for (int i = 0; i < jsonArray.size(); i++) {
            tmp_open = jsonArray.getJSONObject(i).getBigDecimal("open");
            tmp_high = jsonArray.getJSONObject(i).getBigDecimal("high");
            tmp_low = jsonArray.getJSONObject(i).getBigDecimal("low");
            tmp_close = jsonArray.getJSONObject(i).getBigDecimal("close");
            startTime = jsonArray.getJSONObject(i).getBigDecimal("time");
            endTime = startTime.add(type_time);
            log.info("startTime="+startTime);
            log.info("endTime="+endTime);
            System.out.println("startTime="+startTime);
            System.out.println("endTime="+endTime);
            String params_kline_high_low = "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"contract_id\":\""+contranctId+"\"}},{\"range\":{\"created_date\":{\"gte\":\""+startTime+"\",\"lt\":\""+endTime+"\"}}}],\"must_not\":[],\"should\":[]}},\"from\":0,\"size\":1,\"sort\":[],\"aggs\":{\"max_price\":{\"max\":{\"field\":\"price\"}},\"min_price\":{\"min\":{\"field\":\"price\"}}}}";
            String params_kline_close = "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"contract_id\":\""+contranctId+"\"}},{\"range\":{\"created_date\":{\"gte\":\""+startTime+"\",\"lt\":\""+endTime+"\"}}}],\"must_not\":[],\"should\":[]}},\"from\":0,\"size\":5,\"sort\":{\"created_date\":{\"order\":\"desc\"}},\"aggs\":{}}";
            String params_kline_open = "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"contract_id\":\""+contranctId+"\"}},{\"range\":{\"created_date\":{\"gte\":\""+startTime+"\",\"lt\":\""+endTime+"\"}}}],\"must_not\":[],\"should\":[]}},\"from\":0,\"size\":5,\"sort\":{\"created_date\":{\"order\":\"asc\"}},\"aggs\":{}}";

            res_kline_high_low = BaseUtils.postByJson(url,params_kline_high_low);
            res_kline_close = BaseUtils.postByJson(url,params_kline_close);
            res_kline_open = BaseUtils.postByJson(url,params_kline_open);
            try{
                log.info("high："+ JSONObject.parseObject(res_kline_high_low).getJSONObject("aggregations").getJSONObject("max_price").getString("value")+"-----"+tmp_high+"======》"+tmp_high.compareTo(JSONObject.parseObject(res_kline_high_low).getJSONObject("aggregations").getJSONObject("max_price").getBigDecimal("value")));
                log.info("low："+ JSONObject.parseObject(res_kline_high_low).getJSONObject("aggregations").getJSONObject("min_price").getString("value")+"-----"+tmp_low+"======》"+tmp_low.compareTo(JSONObject.parseObject(res_kline_high_low).getJSONObject("aggregations").getJSONObject("min_price").getBigDecimal("value")));
                log.info("close："+JSONObject.parseObject(res_kline_close).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getString("price")+"-----"+tmp_close+"======》"+tmp_close.compareTo(JSONObject.parseObject(res_kline_close).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("price")));
                log.info("open："+JSONObject.parseObject(res_kline_open).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getString("price")+"-----"+tmp_open+"======》"+tmp_open.compareTo(JSONObject.parseObject(res_kline_close).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("price")));
                log.info("======================================");
                System.out.println("high："+ JSONObject.parseObject(res_kline_high_low).getJSONObject("aggregations").getJSONObject("max_price").getString("value")+"-----"+tmp_high+"======》"+tmp_high.compareTo(JSONObject.parseObject(res_kline_high_low).getJSONObject("aggregations").getJSONObject("max_price").getBigDecimal("value")));
                System.out.println("low："+ JSONObject.parseObject(res_kline_high_low).getJSONObject("aggregations").getJSONObject("min_price").getString("value")+"-----"+tmp_low+"======》"+tmp_low.compareTo(JSONObject.parseObject(res_kline_high_low).getJSONObject("aggregations").getJSONObject("min_price").getBigDecimal("value")));
                System.out.println("close："+JSONObject.parseObject(res_kline_close).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getString("price")+"-----"+tmp_close+"======》"+tmp_close.compareTo(JSONObject.parseObject(res_kline_close).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("price")));
                System.out.println("open："+JSONObject.parseObject(res_kline_open).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getString("price")+"-----"+tmp_open+"======》"+tmp_open.compareTo(JSONObject.parseObject(res_kline_close).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("price")));
                System.out.println("======================================");
            }catch (Exception e){
                JSONObject.parseObject(res_kline_high_low).getJSONObject("aggregations").getJSONObject("max_price").getString("value");
                log.info("======================================");
                JSONObject.parseObject(res_kline_high_low).getJSONObject("aggregations").getJSONObject("max_price").getString("value");
                System.out.println("======================================");
            }
        }
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stamp = "";
        if (!"".equals(time)) {//时间不为空
            try {
                stamp = String.valueOf(sdf.parse(time).getTime());
            } catch (Exception e) {
                System.out.println("参数为空！");
            }
        }else {    //时间为空
            long current_time = System.currentTimeMillis();  //获取当前时间
            stamp = String.valueOf(current_time);
        }
        return stamp;
    }


    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(int time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_Date = sdf.format(new Date(time * 1000L));
        return time_Date;

    }
}
