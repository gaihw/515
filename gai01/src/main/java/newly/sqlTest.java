package newly;

import old.Calc.CalcPotential;
import old.Calc.Config;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class sqlTest {
    public static void main(String[] args) {
        String[] sql = {"select * from user_base_info"};
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test","root","123456");
            // 执行查询
            stmt = conn.createStatement();
            System.out.println("run...");
                ResultSet rs = stmt.executeQuery(sql[0]);
                // 展开结果集数据库
                while (rs.next()) {
                    System.out.println("test...");
                    System.out.println(rs.getBigDecimal("mobile"));
                }
                rs.close();
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
