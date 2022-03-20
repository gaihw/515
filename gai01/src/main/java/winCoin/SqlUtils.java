package winCoin;

import java.sql.*;

public class SqlUtils {

    public static ResultSet select(String sql){
        Connection conn = null;
        Statement stmt = null;
//        String sql = "SELECT * FROM `bib_cfd`.`user_position_log` WHERE `id` IN ("+id+") ORDER BY `id` LIMIT 0,1000";
        ResultSet resultSet = null;
        try{
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 打开链接
            conn = DriverManager.getConnection("jdbc:mysql://rm-gs523r33n289871n3.mysql.singapore.rds.aliyuncs.com:3306/","bl_admin_rw","sHl#srOEHsT^Zcl9gv1qm6KmFilL");
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sql);
            return resultSet;
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
//            try{
//                if(stmt!=null) stmt.close();
//            }catch(SQLException se2){
//            }// 什么都不做
//            try{
//                if(conn!=null) conn.close();
//            }catch(SQLException se){
//                se.printStackTrace();
//            }
//            try {
//                if (resultSet!=null) resultSet.close();
//            }catch (SQLException se){
//                se.printStackTrace();
//            }
        }
        return null;
    }
}
