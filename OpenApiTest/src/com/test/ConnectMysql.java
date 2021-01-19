package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectMysql {
//		static final String DB_URL = "jdbc:mysql://192.168.112.36/cfd_account";
		static final String DB_URL = "jdbc:mysql://192.168.112.36/cfd_ams";
	    // MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称
	    static final String USER = "root";
	    static final String PASS = "58test";

	    public static void main(String[] args) throws SQLException,Exception{
	       Connection conn = null;
	       Statement stat = null;
	       ResultSet rs = null;
	       // 注册驱动
//	       Class.forName("com.mysql.jdbc.Driver");
	       Class.forName("com.mysql.cj.jdbc.Driver");
	       // 创建链接
	       conn = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);
	       stat = (Statement) conn.createStatement();
	       //执行插入语句
//	       String sql = "iNSERT INTO `cfd_account`.`tb_account_info`( `user_id`, `currency_id`, `available`, `hold`, `status`, `created_date`, `modify_date`)  VALUES ( 100303, 8, 1000000000, 0,0, now(), now());";
//	       stat.execute(sql);
	       // 执行查询
	       String sql = "SELECT * FROM tb_settle";
	       rs = stat.executeQuery(sql);
	       // 输出查询结果
	       while(rs.next()){
	           System.out.print(rs.getInt("from_user_id")+",");
	       }
	       // 关闭
	      try {
	          if (rs != null) {
	              rs.close();
	          }
	      } catch (SQLException e) {
	          e.printStackTrace();
	      } finally {
	          try {
	              if (stat != null) {
	                  stat.close();
	              }
	          } catch (SQLException e) {
	              e.printStackTrace();
	          } finally {
	              try {
	                  if (conn != null) {
	                      conn.close();
	                  }
	              } catch (SQLException e) {
	                  e.printStackTrace();
	              }
	          }
	      }

	}

}
