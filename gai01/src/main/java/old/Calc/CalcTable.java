package old.Calc;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 统计报表计算
 */
public class CalcTable {
    public static void main(String[] args) throws IOException {
        FileWriter fw = new FileWriter(new File("data/sqlResult_table"));
        String userId = "333387939";
        calcTable(userId,fw);
//        System.out.println(getTicker(4,4001));
//        System.out.println(getTickerByProductId(1,1));
//        System.out.println(chuancangFentan(userId,1));
    }
    public static void calcTable(String userId,FileWriter fw) throws IOException {
        Connection conn = null;
        Statement stmt = null;
        String[] columName = {"profit","max_profit","win_profit","loss_profit"
                ,"win_hand","loss_hand","total_fee","high_frequency_count"
                ,"total_position_time","sum_count","position_time_profit","position_time_loss"
                ,"potential_profit","potential_loss"};
        String[] business_id = {"swaps","future","regular","mix"};
        List<BigDecimal> liqution = liquidation(userId);
        JSONObject data = new JSONObject();
        try{
            // 注册 JDBC 驱动
            Class.forName(Config.JDBC_DRIVER);
            // 打开链接
            System.out.println("run...");
            conn = DriverManager.getConnection(Config.DB_URL_BIGDATA,Config.USER_36,Config.PASS_36);
            // 执行查询
            stmt = conn.createStatement();
            for (int i = 1 ; i <= 4 ; i++) {
                String sql = "select * from tb_bdr_user_kpi where user_id=" + userId + " and business_id= " + i;
                ResultSet rs = stmt.executeQuery(sql);
                // 展开结果集数据库
                while (rs.next()) {
                    data.put(business_id[i-1]+"_"+columName[0],rs.getBigDecimal(columName[0]));
                    data.put(business_id[i-1]+"_"+columName[1],rs.getBigDecimal(columName[1]));
                    data.put(business_id[i-1]+"_"+columName[2],rs.getBigDecimal(columName[2]));
                    data.put(business_id[i-1]+"_"+columName[3],rs.getBigDecimal(columName[3]));
                    data.put(business_id[i-1]+"_"+columName[4],rs.getBigDecimal(columName[4]));
                    data.put(business_id[i-1]+"_"+columName[5],rs.getBigDecimal(columName[5]));
                    data.put(business_id[i-1]+"_"+columName[6],rs.getBigDecimal(columName[6]));
                    data.put(business_id[i-1]+"_"+columName[7],rs.getBigDecimal(columName[7]));
                    data.put(business_id[i-1]+"_"+columName[8],rs.getBigDecimal(columName[8]));
                    data.put(business_id[i-1]+"_"+columName[9],rs.getBigDecimal(columName[9]));
                    data.put(business_id[i-1]+"_"+columName[10],rs.getBigDecimal(columName[10]));
                    data.put(business_id[i-1]+"_"+columName[11],rs.getBigDecimal(columName[11]));
                    data.put(business_id[i-1]+"_"+columName[12],rs.getBigDecimal(columName[12]));
                    data.put(business_id[i-1]+"_"+columName[13],rs.getBigDecimal(columName[13]));
                }
                rs.close();
            }
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        //总的累计盈亏
        BigDecimal sumProfit = new BigDecimal(0);
        //总的风险报酬比
        BigDecimal sumFengxian = new BigDecimal(0);
        //总的持仓时间
        BigDecimal sumPositionTime = new BigDecimal(0);
        //总的真实胜率
        BigDecimal sumRealWinRate = new BigDecimal(0);
        //总的真实盈亏比
        BigDecimal sumRealWinLoss = new BigDecimal(0);
        //总的真实期望值
        BigDecimal sumRealqiwang = new BigDecimal(0);
        //总的潜在胜率
        BigDecimal sumPotentialWinRate = new BigDecimal(0);
        //总的潜在盈亏比
        BigDecimal sumPotentialWinLoss = new BigDecimal(0);
        //总的潜在期望值
        BigDecimal sumPotentialqiwang = new BigDecimal(0);
        //总的爆仓
        BigDecimal sumLiqudation = new BigDecimal(0);
        //总的交易
        BigDecimal sumCount = new BigDecimal(0);
        //总的手续费
        BigDecimal sumFee = new BigDecimal(0);
        //总的高频
        BigDecimal sumgaopin = new BigDecimal(0);
        //穿仓损失
        BigDecimal sumChuancang = new BigDecimal(0);
        //分摊保证金
        BigDecimal sumBaozhengjin = new BigDecimal(0);
        for (int i = 1 ; i <=4 ; i ++){
            //累计盈亏
            BigDecimal profit = data.getBigDecimal(business_id[i-1]+"_"+columName[0]);
            fw.write(business_id[i-1]+"累计盈亏="+profit+"\r\n");
            sumProfit = sumProfit.add(profit);

            //风险报酬比
            BigDecimal fengxian = new BigDecimal(CalcPotential.potential(i,userId));
            fw.write(business_id[i-1]+"风险报酬比="+fengxian+"\r\n");
            sumFengxian = sumFengxian.add(fengxian);

            //持仓时间
            BigDecimal positionTime = data.getBigDecimal(business_id[i-1]+"_"+columName[8]).divide(data.getBigDecimal(business_id[i-1]+"_"+columName[9]),8, RoundingMode.HALF_UP);
            fw.write(business_id[i-1]+"平均持仓时间="+positionTime+"\r\n");
            sumPositionTime = sumPositionTime.add(positionTime);

            //总手数
            BigDecimal sum_hand = data.getBigDecimal(business_id[i-1]+"_"+columName[4]).add(data.getBigDecimal(business_id[i-1]+"_"+columName[5]));
            //盈利手数
            BigDecimal win_hand = data.getBigDecimal(business_id[i-1]+"_"+columName[4]);
            //真实胜率=盈利手数/总手数
            BigDecimal real_winRate = win_hand.divide(sum_hand,8, RoundingMode.HALF_UP);
            fw.write(business_id[i-1]+"真实胜率="+real_winRate.multiply(new BigDecimal(100))+"\r\n");
            sumRealWinRate = sumRealWinRate.add(real_winRate);

            //真实平均盈利=盈利总金额/盈利总手数
            BigDecimal real_pj_win ;
            if (data.getBigDecimal(business_id[i-1]+"_"+columName[4]).compareTo(BigDecimal.ZERO)!=0){
                real_pj_win = data.getBigDecimal(business_id[i-1]+"_"+columName[2]).divide(data.getBigDecimal(business_id[i-1]+"_"+columName[4]),8, RoundingMode.HALF_UP);
            }else{
                real_pj_win = new BigDecimal(0) ;
            }
            //真实平均亏损=亏损总金额/亏损总手数
            BigDecimal real_pj_loss = data.getBigDecimal(business_id[i-1]+"_"+columName[3]).divide(data.getBigDecimal(business_id[i-1]+"_"+columName[5]),8, RoundingMode.HALF_UP).abs();
            //真实盈亏比=平均盈利/平均亏损
            BigDecimal realWinLoss = real_pj_win.divide(real_pj_loss,8, RoundingMode.HALF_UP);
            fw.write(business_id[i-1]+"真实盈亏比="+realWinLoss+"\r\n");
            sumRealWinLoss = sumRealWinLoss.add(realWinLoss);

            //真实期望值=真实胜率*真实平均盈利-（1-真实胜率）*真实平均亏损
            BigDecimal realqiwang = real_winRate.multiply(real_pj_win).subtract((new BigDecimal(1).subtract(real_winRate)).multiply(real_pj_loss));
            fw.write(business_id[i-1]+"真实期望值="+realqiwang+"\r\n");
            sumRealqiwang = sumRealqiwang.add(realqiwang);

            //潜在盈利持仓时间
            BigDecimal position_time_profit = data.getBigDecimal(business_id[i-1]+"_"+columName[10]);
            //潜在亏损持仓时间
            BigDecimal position_time_loss = data.getBigDecimal(business_id[i-1]+"_"+columName[11]);
            //潜在胜率=sum（每笔交易浮盈1分钟K线数*手数）/（sum（每笔交易浮亏1分钟K线数*手数）+sum（每笔交易浮盈1分钟K线数*手数））
            BigDecimal potential_winRate;
            if (position_time_loss.add(position_time_profit).compareTo(BigDecimal.ZERO)!=0) {
                potential_winRate = position_time_profit.divide(position_time_loss.add(position_time_profit), 8, RoundingMode.HALF_UP);
                fw.write(business_id[i - 1] + "潜在胜率=" + potential_winRate.multiply(new BigDecimal(100)) + "\r\n");
                sumPotentialWinRate = sumPotentialWinRate.add(potential_winRate);
            }else{
                potential_winRate = new BigDecimal(0);
                fw.write(business_id[i - 1] + "潜在胜率=" + potential_winRate + "\r\n");
                sumPotentialWinRate = sumPotentialWinRate.add(potential_winRate);
            }

            //潜在盈利
            BigDecimal potential_profit = data.getBigDecimal(business_id[i-1]+"_"+columName[12]);
            //潜在亏损
            BigDecimal potential_loss = data.getBigDecimal(business_id[i-1]+"_"+columName[13]);
            //潜在盈亏比=sum（每笔交易潜在最大盈利）/sum（每笔交易潜在最大亏损）
            BigDecimal potentialWinLoss;
            if (potential_loss.compareTo(BigDecimal.ZERO)!=0) {
                potentialWinLoss = potential_profit.divide(potential_loss, 8, RoundingMode.HALF_UP);
                fw.write(business_id[i - 1] + "潜在盈亏比=" + potentialWinLoss + "\r\n");
                sumPotentialWinLoss = sumPotentialWinLoss.add(potentialWinLoss);
            }else{
                potentialWinLoss = new BigDecimal(0);
                fw.write(business_id[i - 1] + "潜在盈亏比=" + potentialWinLoss + "\r\n");
                sumPotentialWinLoss = sumPotentialWinLoss.add(potentialWinLoss);
            }
            //潜在平均盈利=sum(潜在最大盈利)/sum(fill_size)
            BigDecimal potentail_pj_win = potential_profit.divide(sum_hand,8,RoundingMode.HALF_UP);
            //潜在平均亏损=sum(潜在最大亏损)/sum(fill_size)
            BigDecimal potentail_pj_loss = potential_loss.divide(sum_hand,8,RoundingMode.HALF_UP);
            //潜在期望值=潜在胜率*潜在平均盈利-（1-潜在胜率）*潜在平均亏损
            BigDecimal potentialqiwang = potential_winRate.multiply(potentail_pj_win).subtract((new BigDecimal(1).subtract(potential_winRate)).multiply(potentail_pj_loss));
            fw.write(business_id[i-1]+"潜在期望值="+potentialqiwang+"\r\n");
            sumPotentialqiwang = sumPotentialqiwang.add(potentialqiwang);

            fw.write(business_id[i-1]+"爆仓次数="+liqution.get(i-1)+"\r\n");
            sumLiqudation = sumLiqudation.add(liqution.get(i-1));
            fw.write(business_id[i-1]+"交易次数="+data.getBigDecimal(business_id[i-1]+"_"+columName[9])+"\r\n");
            sumCount = sumCount.add(data.getBigDecimal(business_id[i-1]+"_"+columName[9]));
            fw.write(business_id[i-1]+"累计手续费="+data.getBigDecimal(business_id[i-1]+"_"+columName[6])+"\r\n");
            sumFee = sumFee.add(data.getBigDecimal(business_id[i-1]+"_"+columName[6]));
            fw.write(business_id[i-1]+"高频次数="+data.getBigDecimal(business_id[i-1]+"_"+columName[7])+"\r\n");
            sumgaopin = sumgaopin.add(data.getBigDecimal(business_id[i-1]+"_"+columName[7]));
            fw.write(business_id[i-1]+"穿仓损失="+chuancangFentan(userId,i).get(1)+"\r\n");
            sumChuancang = sumChuancang.add(chuancangFentan(userId,i).get(1));
            fw.write(business_id[i-1]+"分摊保证金="+chuancangFentan(userId,i).get(0)+"\r\n");
            sumBaozhengjin = sumBaozhengjin.add(chuancangFentan(userId,i).get(0));
            fw.write("#########################\r\n");
            fw.flush();
            System.out.println("第"+i+"个业务线已计算完成...");
        }
        fw.write("总的累计盈亏="+sumProfit+"\r\n");
        fw.write("总的风险报酬比="+CalcPotential.potential(0,userId)+"\r\n");
        fw.write("总的持仓时间="+sumPositionTime.divide(new BigDecimal(4),8,RoundingMode.HALF_UP)+"\r\n");
        fw.write("总的真实胜率="+sumRealWinRate.divide(new BigDecimal(4),8,RoundingMode.HALF_UP).multiply(new BigDecimal(100))+"\r\n");
        fw.write("总的真实盈亏比="+sumRealWinLoss.divide(new BigDecimal(4),8,RoundingMode.HALF_UP)+"\r\n");
        fw.write("总的真实期望值="+sumRealqiwang.divide(new BigDecimal(4),8,RoundingMode.HALF_UP)+"\r\n");
        fw.write("总的潜在胜率="+sumPotentialWinRate.divide(new BigDecimal(4),8,RoundingMode.HALF_UP).multiply(new BigDecimal(100))+"\r\n");
        fw.write("总的潜在盈亏比="+sumPotentialWinLoss.divide(new BigDecimal(4),8,RoundingMode.HALF_UP)+"\r\n");
        fw.write("总的潜在期望值="+sumPotentialqiwang.divide(new BigDecimal(4),8,RoundingMode.HALF_UP)+"\r\n");
        fw.write("总的爆仓="+sumLiqudation+"\r\n");
        fw.write("总的交易="+sumCount+"\r\n");
        fw.write("总的手续费="+sumFee+"\r\n");
        fw.write("总的高频="+sumgaopin+"\r\n");
        fw.write("总穿仓损失="+sumChuancang+"\r\n");
        fw.write("总保证金="+sumBaozhengjin+"\r\n");
        fw.flush();
        fw.close();
        System.out.println("over...");
    }
    /**
     * 统计穿仓损失和分摊保证金
     * @param userId
     * @param busenniseId
     * @return
     */
    public static List<BigDecimal> chuancangFentan(String userId, int busenniseId){
        String[] sql = {"select tt.currency_id as currency_id,tt.action as action ,tt.sum_amount as amount from (select currency_id,action ,amount as sum_amount from tb_system_ledger where user_id="+userId+"  and action=22  union all select currency_id,action ,amount as sum_amount from tb_system_ledger_history where user_id="+userId+"  and action=22  ) as tt ",
        "select tt.action as action ,tt.sum_amount as amount from (select action ,amount as sum_amount from tb_system_ledger where user_id="+userId+"  and action in (25,26)  union all select action ,amount as sum_amount from tb_system_ledger_history where user_id="+userId+"  and action in (25,26)  ) as tt "};
        List<BigDecimal> li = new ArrayList<BigDecimal>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("0",new BigDecimal(0));
        jsonObject.put("1",new BigDecimal(0));
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(Config.JDBC_DRIVER);
            // 打开链接
            if (busenniseId == 1) {
                conn = DriverManager.getConnection(Config.DB_URL_58swap, Config.USER_31, Config.PASS_31);
            }else if (busenniseId == 2){
                conn = DriverManager.getConnection(Config.DB_URL_58future, Config.USER_86, Config.PASS_86);
            }else if (busenniseId == 3){
                conn = DriverManager.getConnection(Config.DB_URL_58regular, Config.USER_86, Config.PASS_86);
            }else{
                conn = DriverManager.getConnection(Config.DB_URL_58mix, Config.USER_86, Config.PASS_86);
            }
            // 执行查询
            stmt = conn.createStatement();
            if (busenniseId == 1 ) {
                ResultSet rs = stmt.executeQuery(sql[0]);
                // 展开结果集数据库
                while (rs.next()) {
                    //获取币种
                    int currency_id = rs.getInt("currency_id");
                    BigDecimal ticker = getTickerByProductId(busenniseId,currency_id);
                    //穿仓&分摊
                    int action = rs.getInt("action");
                    //金额
                    BigDecimal amount = rs.getBigDecimal("amount");
                    if (amount.compareTo(BigDecimal.ZERO) == 1) {
                        jsonObject.put("0",jsonObject.getBigDecimal("0").add(amount.multiply(ticker)));
                    }else {
                        jsonObject.put("1",jsonObject.getBigDecimal("1").add(amount.multiply(ticker)));
                    }
                }
                rs.close();

            }else {
                ResultSet rs = stmt.executeQuery(sql[1]);
                // 展开结果集数据库
                while (rs.next()) {
                    //穿仓&分摊
                    int action = rs.getInt("action");
                    //金额
                    BigDecimal amount = rs.getBigDecimal("amount");
                    if (amount.compareTo(BigDecimal.ZERO) == 1) {
                        jsonObject.put("0",jsonObject.getBigDecimal("0").add(amount));
                    }else {
                        jsonObject.put("1",jsonObject.getBigDecimal("1").add(amount));
                    }
                }
                rs.close();
            }
            stmt.close();
            conn.close();
            li.add(jsonObject.getBigDecimal("0"));
            li.add(jsonObject.getBigDecimal("1"));
            return li;
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
            return null;
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
            return null;
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    /**
     * 根据币种id获取指数价格
     * @param busenniseId
     * @param currency_id
     * @return
     */
    public static BigDecimal getTickerByProductId(int busenniseId,int currency_id){
        String sql ;
        BigDecimal ticker = null;
//        if (busenniseId==1){
            sql = "SELECT id FROM tb_swaps_contract WHERE currency_id="+currency_id;
//        }else{
//            sql = "SELECT seq FROM tb_contract WHERE currency_id="+currency_id;
//        }
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(Config.JDBC_DRIVER);
            // 打开链接
//            if (busenniseId == 1) {
                conn = DriverManager.getConnection(Config.DB_URL_58swap, Config.USER_31, Config.PASS_31);
//            }else{
//                conn = DriverManager.getConnection(Config.DB_URL_58mix, Config.USER_86, Config.PASS_86);
//            }
            // 执行查询
            stmt = conn.createStatement();
//            if (busenniseId == 1) {
                ResultSet rs = stmt.executeQuery(sql);
                // 展开结果集数据库
                while (rs.next()) {
                    //获取币种id
                    int contractId = rs.getInt("id");
                    ticker = getTicker(busenniseId,contractId);
                }
                rs.close();
//            }else{
//                ResultSet rs = stmt.executeQuery(sql);
//                 展开结果集数据库
//                while (rs.next()) {
//                    获取币种id
//                    int seq = rs.getInt("seq");
//                    ticker = getTicker(busenniseId,seq);
//                }
//                rs.close();
//            }
            stmt.close();
            conn.close();
            return ticker;
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
            return null;
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
            return null;
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    /**
     * 根据合约id获取指数价格
     * @param busenniseId
     * @param contractId
     * @return
     */
    public static BigDecimal getTicker(int busenniseId,int contractId){
        String sql ;
//        if (busenniseId==1){
            sql = "SELECT last from tb_ticker WHERE product_id = "+contractId+" ORDER BY id desc LIMIT 0,1";
//        }else{
//            sql = "SELECT last from tb_ticker WHERE product_id = "+id+" ORDER BY seq desc LIMIT 0,1";
//        }
        Connection conn = null;
        Statement stmt = null;
        BigDecimal ticker = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(Config.JDBC_DRIVER);
            // 打开链接
//            if (busenniseId == 1) {
                conn = DriverManager.getConnection(Config.DB_URL_58swap, Config.USER_31, Config.PASS_31);
//            }else{
//                conn = DriverManager.getConnection(Config.DB_URL_58mix, Config.USER_86, Config.PASS_86);
//            }
            // 执行查询
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while (rs.next()) {
                //获取币种id对应的指数
                ticker = rs.getBigDecimal("last");
            }
            rs.close();
            stmt.close();
            conn.close();
            return ticker;
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
            return null;
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
            return null;
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
    /**
     * 统计爆仓次数
     * @param userId
     * @return
     */
    public static List<BigDecimal> liquidation(String userId){
        String[] sql = {"select count(*) as li from tb_bdr_swaps_order_fill where liquidation=1 and user_id="+userId,
                "select count(*) as li from tb_future_order_fill where liquidation=1 and user_id="+userId,
                "select count(*)  as li from tb_bdr_regular_future_order_fill where liquidation=1 and user_id="+userId,
                "select count(*) as li from tb_mix_order_fill where liquidation=1 and user_id="+userId};
        List li = new ArrayList();
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(Config.DB_URL_58RISK,Config.USER_36,Config.PASS_36);
            // 执行查询
            stmt = conn.createStatement();
            for (int i = 1 ; i <= 4 ; i++) {
                ResultSet rs = stmt.executeQuery(sql[i-1]);
                // 展开结果集数据库
                while (rs.next()) {
                    li.add(i-1,rs.getBigDecimal("li"));
                }
                rs.close();
            }
            stmt.close();
            conn.close();
            return li;
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
            return null;
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
            return null;
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
}
