package Calc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

public class CalcContractProfit {
    public static void main(String[] args) throws IOException {
        FileWriter fw = new FileWriter(new File("data/sqlResult_contractProfit"));
        String userId = "333387939";
        contractProfit_2(userId,fw);
    }

    /**
     * 统计用户各个业务线各个合约的盈利量
     * @param userId
     * @param fw
     */
    public static void contractProfit_2(String userId,FileWriter fw){
        String [] sql = {"select contract_id,sum(profit) from tb_swaps_fill_close_detail where user_id="+userId+"  GROUP BY contract_id ORDER BY contract_id asc",
                "select contract_id,sum(profit) from tb_future_fill_close_detail where user_id="+userId+"  GROUP BY contract_id ORDER BY contract_id asc",
                "select contract_id,sum(profit) from tb_regular_future_fill_close_detail where user_id="+userId+"  GROUP BY contract_id ORDER BY contract_id asc",
                "select contract_id,sum(profit) from tb_mix_fill_close_detail where user_id="+userId+"  GROUP BY contract_id ORDER BY contract_id asc"};
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(Config.DB_URL_BIGDATA,Config.USER_36,Config.PASS_36);
            // 执行查询
            stmt = conn.createStatement();
            System.out.println("run...");
            for (int i = 1 ; i <= 4 ; i++) {
                ResultSet rs = stmt.executeQuery(sql[i-1]);
                // 展开结果集数据库
                while (rs.next()) {
                    String contractId = rs.getString("contract_id");
                    BigDecimal profit = rs.getBigDecimal("sum(profit)");
                    fw.write(userId+"("+contractId+")="+profit+"\r\n");
                    fw.flush();
                }
                fw.write("############################\r\n");
                fw.flush();
//                System.out.println("#############################");
                rs.close();
            }
            System.out.println("over...");
            fw.close();
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
    }

    /**
     * 统计用户各个业务线各个合约的盈利量
     * @param userId
     * @param fw
     */
    public static void contractProfit_1(String userId,FileWriter fw){
        String[] sql = {"select contract_id from tb_swaps_fill_close_detail where user_id="+userId+" GROUP BY contract_id ORDER BY contract_id asc",
                "select contract_id from tb_future_fill_close_detail where user_id="+userId+" GROUP BY contract_id ORDER BY contract_id asc",
                "select contract_id from tb_regular_future_fill_close_detail where user_id="+userId+" GROUP BY contract_id ORDER BY contract_id asc",
                "select contract_id from tb_mix_fill_close_detail where user_id="+userId+" GROUP BY contract_id ORDER BY contract_id asc"};

        String[] sql_profit = {"select sum(profit) from tb_swaps_fill_close_detail where  user_id="+userId+" and contract_id=",
                "select sum(profit) from tb_future_fill_close_detail where  user_id="+userId+" and contract_id=",
                "select sum(profit) from tb_regular_future_fill_close_detail where  user_id="+userId+" and contract_id=",
                "select sum(profit) from tb_mix_fill_close_detail where  user_id="+userId+" and contract_id="};

        Connection conn = null;
        Statement stmt = null;
        Statement stmt1 = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(Config.DB_URL_BIGDATA,Config.USER_36,Config.PASS_36);
            // 执行查询
            stmt = conn.createStatement();
            stmt1 = conn.createStatement();
            System.out.println("run...");
            for (int i = 1 ; i <= 4 ; i++) {
                ResultSet rs = stmt.executeQuery(sql[i-1]);
                // 展开结果集数据库
                while (rs.next()) {
                    String contractId = rs.getString("contract_id");
                    ResultSet rs_profit = stmt1.executeQuery(sql_profit[i-1]+contractId);
                    while (rs_profit.next()){
                        fw.write(userId+"("+contractId+")="+rs_profit.getBigDecimal("sum(profit)")+"\r\n");
                        fw.flush();
//                        System.out.println(userId+"-"+contractId+"="+rs_profit.getBigDecimal("sum(profit)"));
                    }
                    rs_profit.close();
                }
                fw.write("############################\r\n");
                fw.flush();
//                System.out.println("#############################");
                rs.close();
            }
            System.out.println("over...");
            fw.close();
            stmt.close();
            stmt1.close();
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
    }
}
