package withdrawal;

import Calc.Config;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;

public class CheckData {
    public static void main(String[] args) throws IOException {

        String startTime = "2020-06-18 23:00:02";
        String endTime = "2020-06-19 23:00:02";
        //1-usdt  2-mix 3-regular tb_mix_currency_index tb_user_wallet_snapshot
//        worth_snapshot(1,startTime,endTime);
//        balance_snapshot(1,startTime,endTime);
//        mix_currency_index(startTime,endTime);
        user_wallet_snapshot(startTime,endTime);
    }
    public static void worth_snapshot(int bussiness,String startTime,String endTime){
        Connection conn = null;
        Statement stmt = null;
        Connection conn1 = null;
        Statement stmt1 = null;
        String[] sqls = {"select * from tb_contract_worth_snapshot where create_date  BETWEEN \'"+startTime+"\' and \'"+endTime+"\' ORDER BY user_id desc",
                "select * from tb_mix_contract_worth_snapshot where create_date  BETWEEN \'"+startTime+"\' and \'"+endTime+"\' ORDER BY user_id desc",
                "select * from tb_regular_contract_worth_snapshot where create_date  BETWEEN \'"+startTime+"\' and \'"+endTime+"\' ORDER BY user_id desc"};
        String sql = null;
        if (bussiness==1){
            sql = sqls[0];
        }else if(bussiness==2){
            sql = sqls[1];
        }else if(bussiness==3){
            sql = sqls[2];
        }
        try{
            // 注册 JDBC 驱动
            Class.forName(Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(Config.DB_URL_58STATISTICS,Config.USER_36,Config.PASS_36);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            conn1 = DriverManager.getConnection(Config.DB_URL_58RISK_WITHDRAWAL,Config.USER_36,Config.PASS_36);
            stmt1 = conn1.createStatement();
            ResultSet rs1 = stmt1.executeQuery(sql);


            rs.last();
            rs1.last();
            System.out.println("oldCount="+rs.getRow()+";newCount="+rs1.getRow());
            System.out.println("--------------------------------");
            if (rs.getRow()==rs1.getRow()){
                rs.first();
                rs1.first();
                // 展开结果集数据库
                while(rs.next()&&rs1.next()){
    //                System.out.println(rs.getBigDecimal("stat_day")+"==="+rs1.getBigDecimal("stat_day"));
    //                System.out.println(rs.getBigDecimal("user_id")+"==="+rs1.getBigDecimal("user_id"));
    //                System.out.println(rs.getBigDecimal("contract_id")+"==="+rs1.getBigDecimal("contract_id"));
    //                System.out.println(rs.getBigDecimal("income")+"==="+rs1.getBigDecimal("income"));
    //                System.out.println(rs.getBigDecimal("contract_worth")+"==="+rs1.getBigDecimal("contract_worth"));
    //                System.out.println("--------------------------------");
    //                !rs.getBigDecimal("stat_day").equals(rs1.getBigDecimal("stat_day"))&&
    //                !rs.getBigDecimal("contract_id").equals(rs1.getBigDecimal("contract_id"))&&
    //                !rs.getBigDecimal("user_id").equals(rs1.getBigDecimal("user_id"))&&
                    if (!rs.getBigDecimal("income").equals(rs1.getBigDecimal("income"))&&
                            !rs.getBigDecimal("contract_worth").equals(rs1.getBigDecimal("contract_worth"))){
                        System.out.println(rs.getBigDecimal("stat_day")+"!!!==="+rs1.getBigDecimal("stat_day"));
                        System.out.println(rs.getBigDecimal("user_id")+"!!!==="+rs1.getBigDecimal("user_id"));
                        System.out.println(rs.getBigDecimal("contract_id")+"!!!==="+rs1.getBigDecimal("contract_id"));
                        System.out.println(rs.getBigDecimal("income")+"!!!==="+rs1.getBigDecimal("income"));
                        System.out.println(rs.getBigDecimal("contract_worth")+"!!!==="+rs1.getBigDecimal("contract_worth"));
                        break;
                    }
                    System.out.println("数据一致");
                }
            }else{
                System.out.println("数据量不一致！！！");
            }

            rs.close();
            stmt.close();
            conn.close();
            rs1.close();
            stmt1.close();
            conn1.close();
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
        System.out.println("over...");
    }
    public static void balance_snapshot(int bussiness,String startTime,String endTime){
        Connection conn = null;
        Statement stmt = null;
        Connection conn1 = null;
        Statement stmt1 = null;
        String[] sqls = {"select * from tb_future_balance_snapshot where create_date  BETWEEN \'"+startTime+"\' and \'"+endTime+"\' ORDER BY user_id desc",
                "select * from tb_mix_future_balance_snapshot where create_date  BETWEEN \'"+startTime+"\' and \'"+endTime+"\' ORDER BY user_id desc",
                "select * from tb_regular_future_balance_snapshot where create_date  BETWEEN \'"+startTime+"\' and \'"+endTime+"\' ORDER BY user_id desc"};
        String sql = null;
        if(bussiness==1){
            sql = sqls[0];
        }else if(bussiness==2){
            sql = sqls[1];
        }else if(bussiness==3){
            sql = sqls[2];
        }
        try{
            // 注册 JDBC 驱动
            Class.forName(Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(Config.DB_URL_58STATISTICS,Config.USER_36,Config.PASS_36);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            conn1 = DriverManager.getConnection(Config.DB_URL_58RISK_WITHDRAWAL,Config.USER_36,Config.PASS_36);
            stmt1 = conn1.createStatement();
            ResultSet rs1 = stmt1.executeQuery(sql);

            rs.last();
            rs1.last();
            System.out.println("oldCount="+rs.getRow()+";newCount="+rs1.getRow());
            System.out.println("--------------------------------");
            if (rs.getRow()==rs1.getRow()){
                rs.first();
                rs1.first();
                // 展开结果集数据库
                while(rs.next()&&rs1.next()){
    //                System.out.println(rs.getBigDecimal("stat_day")+"==="+rs1.getBigDecimal("stat_day"));
    //                System.out.println(rs.getBigDecimal("user_id")+"==="+rs1.getBigDecimal("user_id"));
    //                System.out.println(rs.getBigDecimal("total_balance")+"==="+rs1.getBigDecimal("total_balance"));
    //                System.out.println(rs.getBigDecimal("withdraw")+"==="+rs1.getBigDecimal("withdraw"));
    //                System.out.println(rs.getBigDecimal("deposit")+"==="+rs1.getBigDecimal("deposit"));
    //                System.out.println("--------------------------------");
    //                !rs.getBigDecimal("stat_day").equals(rs1.getBigDecimal("stat_day"))&&
    //                !rs.getBigDecimal("user_id").equals(rs1.getBigDecimal("user_id"))&&
                    if (!rs.getBigDecimal("total_balance").equals(rs1.getBigDecimal("total_balance"))&&
                            !rs.getBigDecimal("withdraw").equals(rs1.getBigDecimal("withdraw"))&&
                            !rs.getBigDecimal("deposit").equals(rs1.getBigDecimal("deposit"))){
                        System.out.println(rs.getBigDecimal("stat_day")+"!!!==="+rs1.getBigDecimal("stat_day"));
                        System.out.println(rs.getBigDecimal("user_id")+"!!!==="+rs1.getBigDecimal("user_id"));
                        System.out.println(rs.getBigDecimal("total_balance")+"!!!==="+rs1.getBigDecimal("total_balance"));
                        System.out.println(rs.getBigDecimal("withdraw")+"!!!==="+rs1.getBigDecimal("withdraw"));
                        System.out.println(rs.getBigDecimal("deposit")+"!!!==="+rs1.getBigDecimal("deposit"));
                        break;
                    }
                    System.out.println("数据一致");
                }
            }else{
                System.out.println("数据量不一致！！！");
            }
            rs.close();
            stmt.close();
            conn.close();
            rs1.close();
            stmt1.close();
            conn1.close();
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
        System.out.println("over...");
    }public static void mix_currency_index(String startTime,String endTime){
        Connection conn = null;
        Statement stmt = null;
        Connection conn1 = null;
        Statement stmt1 = null;
        String sql = "select * from tb_mix_currency_index where create_date  BETWEEN \'"+startTime+"\' and \'"+endTime+"\' ORDER BY id desc";
        try{
            // 注册 JDBC 驱动
            Class.forName(Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(Config.DB_URL_58STATISTICS,Config.USER_36,Config.PASS_36);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            conn1 = DriverManager.getConnection(Config.DB_URL_58RISK_WITHDRAWAL,Config.USER_36,Config.PASS_36);
            stmt1 = conn1.createStatement();
            ResultSet rs1 = stmt1.executeQuery(sql);

            rs.last();
            rs1.last();
            System.out.println("oldCount="+rs.getRow()+";newCount="+rs1.getRow());
            System.out.println("--------------------------------");
            if (rs.getRow()==rs1.getRow()){
                rs.first();
                rs1.first();
                // 展开结果集数据库
                while(rs.next()&&rs1.next()){
    //                System.out.println(rs.getBigDecimal("stat_day")+"==="+rs1.getBigDecimal("stat_day"));
    //                System.out.println(rs.getBigDecimal("currency_id")+"==="+rs1.getBigDecimal("currency_id"));
    //                System.out.println(rs.getBigDecimal("index_price")+"==="+rs1.getBigDecimal("index_price"));
    //                System.out.println("--------------------------------");
    //                !rs.getBigDecimal("stat_day").equals(rs1.getBigDecimal("stat_day"))&&
                    if (!rs.getBigDecimal("currency_id").equals(rs1.getBigDecimal("currency_id"))&&
                            !rs.getBigDecimal("index_price").equals(rs1.getBigDecimal("index_price"))){
                        System.out.println(rs.getBigDecimal("stat_day")+"!!!==="+rs1.getBigDecimal("stat_day"));
                        System.out.println(rs.getBigDecimal("currency_id")+"!!!==="+rs1.getBigDecimal("currency_id"));
                        System.out.println(rs.getBigDecimal("index_price")+"!!!==="+rs1.getBigDecimal("index_price"));
                        break;
                    }
                    System.out.println("数据一致");
                }
            }else{
                System.out.println("数据量不一致！！！");
            }
            rs.close();
            stmt.close();
            conn.close();
            rs1.close();
            stmt1.close();
            conn1.close();
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
        System.out.println("over...");
    }
    public static void user_wallet_snapshot(String startTime,String endTime){
        Connection conn = null;
        Statement stmt = null;
        Connection conn1 = null;
        Statement stmt1 = null;
        String sql = "select * from tb_user_wallet_snapshot where create_date  BETWEEN \'"+startTime+"\' and \'"+endTime+"\' ORDER BY user_id desc";
        try{
            // 注册 JDBC 驱动
            Class.forName(Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(Config.DB_URL_58STATISTICS,Config.USER_36,Config.PASS_36);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            conn1 = DriverManager.getConnection(Config.DB_URL_58RISK_WITHDRAWAL,Config.USER_36,Config.PASS_36);
            stmt1 = conn1.createStatement();
            ResultSet rs1 = stmt1.executeQuery(sql);
            rs.last();
            rs1.last();
            System.out.println("oldCount="+rs.getRow()+";newCount="+rs1.getRow());
            System.out.println("--------------------------------");
            if (rs.getRow()==rs1.getRow()){
                rs.first();
                rs1.first();
                // 展开结果集数据库
                while(rs.next()&&rs1.next()){
    //                System.out.println(rs.getBigDecimal("stat_day")+"==="+rs1.getBigDecimal("stat_day"));
    //                System.out.println(rs.getBigDecimal("user_id")+"==="+rs1.getBigDecimal("user_id"));
    //                System.out.println(rs.getBigDecimal("site_id")+"==="+rs1.getBigDecimal("site_id"));
    //                System.out.println(rs.getBigDecimal("currency_id")+"==="+rs1.getBigDecimal("currency_id"));
    //                System.out.println(rs.getBigDecimal("balance")+"==="+rs1.getBigDecimal("balance"));
    //                System.out.println(rs.getBigDecimal("withdraw")+"==="+rs1.getBigDecimal("withdraw"));
    //                System.out.println(rs.getBigDecimal("deposit")+"==="+rs1.getBigDecimal("deposit"));
    //                System.out.println("--------------------------------");
    //                !rs.getBigDecimal("stat_day").equals(rs1.getBigDecimal("stat_day"))&&
    //                !rs.getBigDecimal("site_id").equals(rs1.getBigDecimal("site_id"))&&
    //                !rs.getBigDecimal("currency_id").equals(rs1.getBigDecimal("currency_id"))&&
//                    !rs.getBigDecimal("user_id").equals(rs1.getBigDecimal("user_id"))&&
                    if (!rs.getBigDecimal("balance").setScale(6, BigDecimal.ROUND_HALF_UP).equals(rs1.getBigDecimal("balance").setScale(6, BigDecimal.ROUND_HALF_UP))&&
                            !rs.getBigDecimal("withdraw").setScale(6, BigDecimal.ROUND_HALF_UP).equals(rs1.getBigDecimal("withdraw").setScale(6, BigDecimal.ROUND_HALF_UP))&&
                            !rs.getBigDecimal("deposit").setScale(6, BigDecimal.ROUND_HALF_UP).equals(rs1.getBigDecimal("deposit").setScale(6, BigDecimal.ROUND_HALF_UP))){
                        System.out.println(rs.getBigDecimal("stat_day")+"!!!==="+rs1.getBigDecimal("stat_day"));
                        System.out.println(rs.getBigDecimal("user_id")+"!!!==="+rs1.getBigDecimal("user_id"));
                        System.out.println(rs.getBigDecimal("site_id")+"!!!==="+rs1.getBigDecimal("site_id"));
                        System.out.println(rs.getBigDecimal("currency_id")+"!!!==="+rs1.getBigDecimal("currency_id"));
                        System.out.println(rs.getBigDecimal("balance")+"!!!==="+rs1.getBigDecimal("balance"));
                        System.out.println(rs.getBigDecimal("withdraw")+"!!!==="+rs1.getBigDecimal("withdraw"));
                        System.out.println(rs.getBigDecimal("deposit")+"!!!==="+rs1.getBigDecimal("deposit"));
                        break;
                    }
                    System.out.println("数据一致");
                }
            }else{
                System.out.println("数据量不一致！！！");
            }
            rs.close();
            stmt.close();
            conn.close();
            rs1.close();
            stmt1.close();
            conn1.close();
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
        System.out.println("over...");
    }
}
