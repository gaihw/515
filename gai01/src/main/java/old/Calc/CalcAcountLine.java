package old.Calc;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class CalcAcountLine {
    public static void main(String[] args) {
        String userId = "333387939";
        String[] sql = {"SELECT @cPro := @cPro + profit AS 'count_profit' ,close_fill_time FROM (SELECT  @cPro := 0, t.profit,t.close_fill_time from (SELECT profit,close_fill_time from tb_future_fill_close_detail WHERE user_id="+userId+" UNION all SELECT profit,close_fill_time from tb_mix_fill_close_detail WHERE user_id="+userId+"  UNION all SELECT profit,close_fill_time from tb_regular_future_fill_close_detail WHERE user_id="+userId+"  union all SELECT profit,close_fill_time from tb_swaps_fill_close_detail WHERE user_id="+userId+" ) t ORDER BY t.close_fill_time asc) t1",
                "SELECT @cPro := @cPro + profit AS 'count_profit' FROM (SELECT  @cPro := 0, user_id, profit FROM tb_swaps_fill_close_detail WHERE user_id="+userId+" ORDER BY close_fill_time ASC) t1",
        "SELECT @cPro := @cPro + profit AS 'count_profit' FROM (SELECT  @cPro := 0, user_id, profit FROM tb_future_fill_close_detail WHERE user_id="+userId+" ORDER BY close_fill_time ASC) t1",
        "SELECT @cPro := @cPro + profit AS 'count_profit' FROM (SELECT  @cPro := 0, user_id, profit FROM tb_regular_future_fill_close_detail WHERE user_id="+userId+" ORDER BY close_fill_time ASC) t1",
        "SELECT @cPro := @cPro + profit AS 'count_profit' FROM (SELECT  @cPro := 0, user_id, profit FROM tb_mix_fill_close_detail WHERE user_id="+userId+" ORDER BY close_fill_time ASC) t1"};
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
            for (int i = 0 ; i <= 4 ; i++) {
                ResultSet rs = stmt.executeQuery(sql[i]);
                List<BigDecimal> list = CalcPotential.potential_countProfit(i,userId);
                int j = 0 ;
                // 展开结果集数据库
                while (rs.next()) {
                    BigDecimal profit = rs.getBigDecimal("count_profit");
                    System.out.println("profit="+profit+"::"+list.get(j)+"=countProfit");
                    if (profit.compareTo(list.get(j))!=0){
                        System.out.println("error---->"+rs.getTime("close_fill_time")+"profit="+profit+"::"+list.get(j)+"=countProfit");
                    }
                    j ++ ;
                }
                System.out.println("###################第"+i+"个业务线校验通过###################");
                rs.close();
            }
            System.out.println("over...");
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
}
