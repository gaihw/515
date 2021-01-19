package utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MysqlConnector {
	// 声明Connection对象
	static Connection con;

	public static void dbConnect(String driver, String url, String userName, String passWord) throws IOException {

		// 连接数据库
		try {
			// 加载驱动程序
			Class.forName(driver);
			// getConnection()方法，连接MySQL数据库！！
			con = DriverManager.getConnection(url, userName, passWord);
			if (!con.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			System.out.println("----------------------------------------");
		} catch (ClassNotFoundException e) {
			// 数据库驱动类异常处理
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			// 数据库连接失败异常处理
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 将查询结果转换成ArrayList
	public static ArrayList<String> sqlRsToArr(String sql, int i) throws SQLException {
		String sqlResult = null;
		ArrayList<String> resultArr = new ArrayList<String>();

		Statement statement = con.createStatement();
		ResultSet results = statement.executeQuery(sql);
		try {
			while (results.next()) {
				sqlResult = results.getString(i);
				resultArr.add(sqlResult);
			}
		} catch (Exception e) {
			resultArr.add("0");
		}
		results.close();
		return resultArr;
	}

	// 将查询结果转换成ArrayList<Double>
	public static ArrayList<Double> sqlRsToDoubleArr(String sql, int i) throws SQLException {
		double sqlResult = 0;
		ArrayList<Double> resultArr = new ArrayList<Double>();

		Statement statement = con.createStatement();
		ResultSet results = statement.executeQuery(sql);
		try {
			while (results.next()) {
				sqlResult = results.getDouble(i);
				resultArr.add(sqlResult);
			}
		} catch (Exception e) {
			resultArr.add(0.0);
		}
		results.close();
		return resultArr;
	}

	// 将查询结果转换成double
	public static double sqlRsToDouble(String sql, int i) throws SQLException {
		Statement statement = con.createStatement();
		ResultSet results = statement.executeQuery(sql);
		double sqlResult = 0;
		try {
			while (results.next()) {
				sqlResult = results.getDouble(i);
			}
		} catch (Exception e) {
			sqlResult = 0;
		}
		results.close();
		return sqlResult;
	}

	// 将查询结果转换成String
	public static String sqlRsToString(String sql, int i) throws SQLException {
		Statement statement = con.createStatement();
		ResultSet results = statement.executeQuery(sql);
		String sqlResult = null;
		try {
			while (results.next()) {
				sqlResult = results.getString(i);
			}
		} catch (Exception e) {
			sqlResult = "0";
		}
		results.close();
		return sqlResult;
	}

	public static void dbClose() throws SQLException {
		con.close();
	}
}
