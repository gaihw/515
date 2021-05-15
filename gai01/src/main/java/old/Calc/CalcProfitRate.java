package old.Calc;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;

/**
 * 计算收益量和收益率
 */
public class CalcProfitRate {
    static {
        System.setProperty("fileName", "data/clc.log");
    }
    public static Logger log = LoggerFactory.getLogger(CalcProfitRate.class);
    public static void main(String[] args) throws SQLException {

        String userId = "50247";
        String url_acount = "http://192.168.200.86:8883/admin/risk/asset?userId="+userId;
        BigDecimal balance = JSONObject.parseObject(BaseUtils.getByForm(url_acount)).getJSONObject("data").getJSONObject("asset").getBigDecimal("balance");
        BigDecimal positionFrozen = JSONObject.parseObject(BaseUtils.getByForm(url_acount)).getJSONObject("data").getJSONObject("asset").getBigDecimal("positionFrozen");
        BigDecimal orderFrozen = JSONObject.parseObject(BaseUtils.getByForm(url_acount)).getJSONObject("data").getJSONObject("asset").getBigDecimal("orderFrozen");
        BigDecimal nowBalance = balance.add(positionFrozen).add(orderFrozen);
        BigDecimal acount = BigDecimal.ZERO.add(nowBalance);
        ResultSet rs = balance(userId);
        while (rs.next()){
            acount = acount.add(rs.getBigDecimal("total_balance"));
        }
        rs.last();
        BigDecimal day = BigDecimal.valueOf(rs.getRow()+1);


        //日均资产
        BigDecimal totalAcount = acount.divide(day,8, RoundingMode.HALF_UP);
        System.out.println("totalAcount="+totalAcount);
//
//        //计算累计盈利量，累计盈利率，权重
//        String response_worth = BaseUtils.postByForm(url_worth,param_worth);
//        JSONArray j_data1 = JSONArray.parseArray(response_worth);
//        //收益量
//        JSONObject income = new JSONObject();
//        //合约价值
//        JSONObject contractWorth = new JSONObject();
//        //总的收益量
//        BigDecimal total_income = new BigDecimal("0.00000000");
//        //总的合约价值
//        BigDecimal total_worth = new BigDecimal("0.00000000");
//        for (int i = 0 ; i < j_data1.size() ; i ++){
//            String contractId = j_data1.getJSONObject(i).getString("contractId");
//            if (!income.containsKey(contractId)) {
//                income.put(contractId,"0.00000000");
//                contractWorth.put(contractId,"0.00000000");
//                income.put(contractId,income.getBigDecimal(contractId).add(j_data1.getJSONObject(i).getBigDecimal("income")));
//                contractWorth.put(contractId,contractWorth.getBigDecimal(contractId).add(j_data1.getJSONObject(i).getBigDecimal("contractWorth")));
//            }else{
//                income.put(contractId,income.getBigDecimal(contractId).add(j_data1.getJSONObject(i).getBigDecimal("income")));
//                contractWorth.put(contractId,contractWorth.getBigDecimal(contractId).add(j_data1.getJSONObject(i).getBigDecimal("contractWorth")));
//            }
//            total_income = total_income.add(j_data1.getJSONObject(i).getBigDecimal("income"));
//            total_worth = total_worth.add(j_data1.getJSONObject(i).getBigDecimal("contractWorth"));
//        }
//        System.out.println("总收益率("+total_income.divide(totalAcount,8, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))+")；总收益量("+total_income+")");
//        Set contract = income.keySet();
//        Iterator it= contract.iterator();
//        while (it.hasNext()){
//            String contractKey = (String) it.next();
//            //权重*总资产
//            BigDecimal xa = contractWorth.getBigDecimal(contractKey).divide(total_worth,8, RoundingMode.HALF_UP).multiply(totalAcount);
//            System.out.println(contractKey+"收益率("+income.getBigDecimal(contractKey).divide(xa,8, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))+")；收益量("+
//                    income.getBigDecimal(contractKey)+")");
//        }
    }
    public static ResultSet balance(String userId){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = " SELECT * from 58statistics.tb_future_balance_snapshot WHERE user_id ="+userId;
        try{
            // 注册 JDBC 驱动
            Class.forName(Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(Config.DB_URL_36,Config.USER_36,Config.PASS_36);
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
}
