package Calc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static Calc.BaseUtils.writerData1;

public class UsdtData {
    static {
        System.setProperty("fileName", "usdtData/usdtData.log");
    }
    public static Logger log = LoggerFactory.getLogger(UsdtData.class);
    public static void main(String[] args) throws SQLException, IOException {
        String contranctId = "1002";
        String startTime = "2020-12-12 00:00:00";
        String endTime = "2020-12-12 14:00:00";


        BigDecimal tmp_id ;
        BigDecimal tmp_user_id;
        BigDecimal tmp_contranct_id;
        BigDecimal tmp_size;
        BigDecimal tmp_price;
        BigDecimal tmp_side;
        Date tmp_created_date;
        ResultSet re1 = selectEveryTwo(contranctId,startTime,endTime);
        while (re1.next()){
            System.out.println("58Count="+re1.getString("c"));
        }

        ResultSet re2 = selectSum(contranctId,startTime,endTime);
        while (re2.next()){
            System.out.println("58sum(lot)="+re2.getBigDecimal("sum").setScale(0));
        }
//
        String url = "https://hqesonline.9188zhibo.com/future_usdt_real_time_deals/_search";
        String params = "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"contract_id\":\""+contranctId+"\"}},{\"range\":{\"created_date\":{\"gte\":\""+dateToStamp(startTime)+"000\",\"lt\":\""+dateToStamp(endTime)+"000\"}}}],\"must_not\":[],\"should\":[]}},\"from\":0,\"size\":10000,\"sort\":{},\"aggs\":{}}";
        String params_size = "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"contract_id\":\""+contranctId+"\"}},{\"range\":{\"created_date\":{\"gte\":\""+dateToStamp(startTime)+"000\",\"lt\":\""+dateToStamp(endTime)+"000\"}}}],\"must_not\":[],\"should\":[]}},\"from\":0,\"size\":1,\"sort\":[],\"aggs\":{\"sum_size\":{\"sum\":{\"field\":\"size\"}},\"min_price\":{\"min\":{\"field\":\"price\"}},\"max_price\":{\"max\":{\"field\":\"price\"}}}}";

        try{
            log.info("esCount="+JSONObject.parseObject(BaseUtils.postByJson(url,params)).getJSONObject("hits").getBigDecimal("total"));
            log.info("---------------------------------------------");
            System.out.println("esCount="+JSONObject.parseObject(BaseUtils.postByJson(url,params)).getJSONObject("hits").getJSONArray("hits").size());

        }catch (Exception e){
            System.out.println("esCount=0");
        }
        try {
            String res_size = BaseUtils.postByJson(url,params_size);
            System.out.println("es成交量："+ JSONObject.parseObject(res_size).getJSONObject("aggregations").getJSONObject("sum_size").getString("value"));
            System.out.println("---------------------------------------------");
        }catch (Exception e){
            System.out.println("es成交量=0");
        }

        ResultSet re = selectEveryOne(contranctId,startTime,endTime);
        //查询每一条是否一致
        while (re.next()){
            tmp_id = re.getBigDecimal("id");
            tmp_user_id = re.getBigDecimal("user_id");
            tmp_contranct_id = re.getBigDecimal("currency_pair_id");
            tmp_size = re.getBigDecimal("lot");
            tmp_price = re.getBigDecimal("price");
            tmp_side = re.getBigDecimal("trade_direction");
            tmp_created_date = re.getDate("created_date");

            String param = "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"id\":\""+tmp_id+"\"}}],\"must_not\":[],\"should\":[]}},\"from\":0,\"size\":10,\"sort\":[],\"aggs\":{}}";
            String res = BaseUtils.postByJson(url,param);
            try {
                log.info("id：" + JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getString("id") + "---" + tmp_id + "====>" + tmp_id.compareTo(JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("id")));
                log.info("user_id：" + JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("user_id") + "---" + tmp_user_id + "====>" + tmp_user_id.compareTo(JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("user_id")));
                log.info("contract_id：" + JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("contract_id") + "---" + tmp_contranct_id + "====>" + tmp_contranct_id.compareTo(JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("contract_id")));
                log.info("size：" + JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("size") + "---" + tmp_size + "====>" + tmp_size.compareTo(JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("size")));
                log.info("price：" + JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("price") + "---" + tmp_price + "====>" + tmp_price.compareTo(JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("price")));
                log.info("side：" + JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("side") + "---" + tmp_side + "====>" + tmp_side.compareTo(JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("side")));
                log.info("created_date：" + JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("created_date") + "---" + tmp_created_date + "====>" + tmp_created_date);
                log.info("=====================================");
                System.out.println("id：" + JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getString("id") + "---" + tmp_id + "====>" + tmp_id.compareTo(JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("id")));
                System.out.println("user_id：" + JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("user_id") + "---" + tmp_user_id + "====>" + tmp_user_id.compareTo(JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("user_id")));
                System.out.println("contract_id：" + JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("contract_id") + "---" + tmp_contranct_id + "====>" + tmp_contranct_id.compareTo(JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("contract_id")));
                System.out.println("size：" + JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("size") + "---" + tmp_size + "====>" + tmp_size.compareTo(JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("size")));
                System.out.println("price：" + JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("price") + "---" + tmp_price + "====>" + tmp_price.compareTo(JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("price")));
                System.out.println("side：" + JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("side") + "---" + tmp_side + "====>" + tmp_side.compareTo(JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("side")));
                System.out.println("created_date：" + JSONObject.parseObject(res).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getBigDecimal("created_date") + "---" + tmp_created_date + "====>" + tmp_created_date);
                System.out.println("=====================================");
            }catch (Exception e){
                log.info(tmp_id+"......no.......");
                log.info("**************************************");
                System.out.println(tmp_id+"......no.......");
                System.out.println("**************************************");
                writerData1("noid.txt",tmp_id.toString());
            }

        }

        writerData1("noid.txt","00000000000000000000000000000000000000000000000000000000000000000000000000000");
    }

    //查询每条数据
    public static ResultSet selectEveryOne(String contranctId,String startTime,String endTime){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = " select * from liquidation_order_fill where currency_pair_id="+contranctId+" and created_date>=\'"+startTime+ "\' and created_date<\'"+endTime+"\' and liquidity=2 and order_type=0";
        try{
            // 注册 JDBC 驱动
            Class.forName(Calc.Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection("jdbc:mysql://reusdtaws-ap-northeast-1c.cixzhflix3on.ap-northeast-1.rds.amazonaws.com/exchange", "luniu", "JmZF2KVXjAoQpyXI");
            // 执行查询
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            return rs;
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
        return null;
    }
    public static ResultSet selectEveryTwo(String contranctId,String startTime,String endTime){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = " select count(*) as c from liquidation_order_fill where currency_pair_id="+contranctId+" and created_date>=\'"+startTime+ "\' and created_date<\'"+endTime+"\' and liquidity=2 and order_type=0";
        try{
            // 注册 JDBC 驱动
            Class.forName(Calc.Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection("jdbc:mysql://reusdtaws-ap-northeast-1c.cixzhflix3on.ap-northeast-1.rds.amazonaws.com/exchange", "luniu", "JmZF2KVXjAoQpyXI");
            // 执行查询
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            return rs;
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
        return null;
    }

    //查询一个时间总的条数
    public static ResultSet selectSum(String contranctId,String startTime,String endTime){
        Connection conn1 = null;
        Statement stmt1 = null;
        ResultSet rs1 = null;
        String sql = " select sum(lot) as sum from liquidation_order_fill where currency_pair_id="+contranctId+" and created_date>=\'"+startTime+ "\' and created_date<\'"+endTime+"\' and liquidity=2";
        try{
            // 注册 JDBC 驱动
            Class.forName(Calc.Config.JDBC_DRIVER);
            // 打开链接
            conn1 = DriverManager.getConnection("jdbc:mysql://reusdtaws-ap-northeast-1c.cixzhflix3on.ap-northeast-1.rds.amazonaws.com/exchange", "luniu", "JmZF2KVXjAoQpyXI");
            // 执行查询
            stmt1 = conn1.createStatement();
            rs1 = stmt1.executeQuery(sql);
            return rs1;
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
        return null;
    }




    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stamp = "";
        if (!"".equals(time)) {//时间不为空
            try {
                stamp = String.valueOf(sdf.parse(time).getTime()/1000);
            } catch (Exception e) {
                System.out.println("参数为空！");
            }
        }else {    //时间为空
            long current_time = System.currentTimeMillis();  //获取当前时间
            stamp = String.valueOf(current_time/1000);
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
