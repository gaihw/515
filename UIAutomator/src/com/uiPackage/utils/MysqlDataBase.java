package com.uiPackage.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlDataBase {
	public static int sql(String database,String user,String password,String sql){
    	String DB_URL = "jdbc:mysql://"+database;
    	String USER = user;
    	String PASS = password;
       Connection conn = null;
       Statement stat = null;
       ResultSet rs = null;
       // 注册驱动
       try {
		Class.forName("com.mysql.jdbc.Driver");
		conn = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);
		stat = (Statement) conn.createStatement();
		if(sql.contains("INSERT")) {			
			stat.execute(sql);
		}else if(sql.contains("update")) {
			stat.executeUpdate(sql);
		}else if(sql.contains("select")) {
			if(sql.contains("count(*)")) {
				rs = stat.executeQuery(sql);
				// 输出查询结果
				while(rs.next()){
					return rs.getInt("count(*)");
//	           System.out.print(rs.getInt("from_user_id")+",");
				}
			}else if(sql.contains("id")){			
				rs = stat.executeQuery(sql);
				// 输出查询结果
				while(rs.next()){
					return rs.getInt("id");
//	           System.out.print(rs.getInt("from_user_id")+",");
				}
			}else if(sql.contains("keywords")) {
				rs = stat.executeQuery(sql);
				// 输出查询结果
				while(rs.next()){
					return rs.getInt("keywords");
//	           System.out.print(rs.getInt("from_user_id")+",");
				}
			}
		}else {
			stat.execute(sql);
		}
	} catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       // 创建链接
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
      return 0;
    }
}
